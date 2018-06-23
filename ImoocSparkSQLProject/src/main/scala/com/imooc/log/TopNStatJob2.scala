package com.imooc.log

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ListBuffer

/**
  * TopN统计Spark作业：复用已有的数据
  */
object TopNStatJob2 {

  def main(args: Array[String]): Unit = {

    //分区字段类型推测：spark.sql.sources.partitionColumnTypeInference.enabled
    val spark = SparkSession.builder().appName("TopNStatJob")
      .config("spark.sql.sources.partitionColumnTypeInference.enabled", false)
      .master("local[2]").getOrCreate()

    //DataFrame读数据
    val accessDF = spark.read.format("parquet").load("src/main/data/log/clean")
    //accessDF.printSchema()
    //accessDF.show(false)

    val day = "20170511"

    import spark.implicits._
    val commonDF = accessDF.filter($"day" === day && $"cmsType" === "video")
    commonDF.cache()

    StatDAO.deleteData(day)

    //最受欢迎的TopN课程
    videoAccessTopNStat(spark, commonDF)

    //按照地市进行统计TopN课程
    cityAccessTopNStat(spark, commonDF)

    //按照流量进行统计
    videoTrafficsTopNStat(spark, commonDF)

    commonDF.unpersist(true)

    spark.stop()

  }

  /**
    * 最受欢迎的TopN课程
    *
    * @param spark
    * @param commonDF
    */
  def videoAccessTopNStat(spark: SparkSession, commonDF: DataFrame): Unit = {
    import spark.implicits._
    //方法一：DataFrame进行统计
    val videoAccessTopNDF = commonDF
      .groupBy("day", "cmsId").agg(count("cmsId").as("times")).orderBy($"times".desc)
    //videoAccessTopNDF.show(false)

    //方法二：Sql进行统计
    /*
    accessDF.createOrReplaceTempView("access_logs")
    val videoAccessTopNSQL = spark.sql("select day,cmsId,count(1) as times from access_logs " +
      "where day='20170511' and cmsType='video' " +
      "group by day,cmsId " +
      "order by times")

    */

    //videoAccessTopNSQL.show(false)

    /**
      * 将统计结果写入到MySQL中
      * 先Partion--->再partition中每行数据foreach
      */
    try {
      videoAccessTopNDF.foreachPartition(partitionOdRecords => {

        val list = new ListBuffer[DayVideoAccessStat]

        partitionOdRecords.foreach(info => {
          val day = info.getAs[String]("day")
          val cmsId = info.getAs[Long]("cmsId")
          val times = info.getAs[Long]("times")

          /**
            * 不建议大家再此处进行数据库的数据插入
            */

          list.append(DayVideoAccessStat(day, cmsId, times))
        })

        //一次将一个partition的数据插入到Mysql中
        StatDAO.insertDayVideoAccessTopN(list)
      })
    }
    catch {
      case e: Exception => e.printStackTrace()
    }


  }

  /**
    * 按照地市进行统计TopN课程
    *
    * @param spark
    * @param commonDF
    */
  def cityAccessTopNStat(spark: SparkSession, commonDF: DataFrame): Unit = {
    import spark.implicits._

    //方法一：DataFrame进行统计
    val cityAccessTopNDF = commonDF
      .groupBy("day", "city", "cmsId")
      .agg(count("cmsId").as("times"))

    //cityAccessTopNDF.show(false)

    //Window函数在Spark SQL的使用
    val top3 = cityAccessTopNDF.select(
      cityAccessTopNDF("day"),
      cityAccessTopNDF("city"),
      cityAccessTopNDF("cmsId"),
      cityAccessTopNDF("times"),
      row_number().over(Window.partitionBy(cityAccessTopNDF("city"))
        .orderBy(cityAccessTopNDF("times").desc)).as("times_rank")
    ).filter("times_rank <= 3") //.show(false)
    //Top3

    /**
      * 将统计结果写入到MySQL中
      */
    try {
      top3.foreachPartition(partitionOdRecords => {

        val list = new ListBuffer[DayCityViedoAccessStat]

        partitionOdRecords.foreach(info => {
          val day = info.getAs[String]("day")
          val cmsId = info.getAs[Long]("cmsId")
          val city = info.getAs[String]("city")
          val times = info.getAs[Long]("times")
          val timesRank = info.getAs[Int]("times_rank")

          list.append(DayCityViedoAccessStat(day, cmsId, city, times, timesRank))
        })
        StatDAO.insertDayCityVideoAccessTopN(list)
      })
    }
    catch {
      case e: Exception => e.printStackTrace()
    }


  }


  /** 按照流量进行统计
    *
    * @param spark
    * @param commonDF
    */
  def videoTrafficsTopNStat(spark: SparkSession, commonDF: DataFrame): Unit = {

    import spark.implicits._
    val cityAccessTopNDF = commonDF
      .groupBy("day", "cmsId")
      .agg(sum("traffic").as("traffics"))
      .orderBy($"traffics".desc)
    //.show(false)

    /**
      * 将统计结果写入到MySQL中
      */
    try {
      cityAccessTopNDF.foreachPartition(partitionOfRecords => {
        val list = new ListBuffer[DayVideoTrafficsStat]

        partitionOfRecords.foreach(info => {
          val day = info.getAs[String]("day")
          val cmsId = info.getAs[Long]("cmsId")
          val traffics = info.getAs[Long]("traffics")
          list.append(DayVideoTrafficsStat(day, cmsId, traffics))
        })
        StatDAO.insertDayVideoTrafficsAccessTopN(list)
      })
    }
    catch {
      case e: Exception => e.printStackTrace()
    }

  }

}
