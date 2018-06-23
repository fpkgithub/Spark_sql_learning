package com.imooc.log

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * 使用Spark完成我们的数据清洗操作
  */
object SparkStatCleanJob {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("SparkStatCleanJob")
        .config("spark.sql.parquet.compression.codec","gzip")
      .master("local[2]").getOrCreate()

    //输入：src/main/data/log/access.log
    val accessRDD = spark.sparkContext.textFile("src/main/data/log/access.log")

    //accessRDD.take(10).foreach(println)

    //将RDD => DataFrame
    val accessDF = spark.createDataFrame(accessRDD.map(x => AccessConvertUtil.parseLog(x)), AccessConvertUtil.struct)

    //accessDF.printSchema()
    //accessDF.show(false)

    //存储数据
    accessDF.coalesce(1).write.format("parquet").partitionBy("day").mode(SaveMode.Overwrite).save("src/main/data/log/clean2")

    spark.stop()

  }

}
