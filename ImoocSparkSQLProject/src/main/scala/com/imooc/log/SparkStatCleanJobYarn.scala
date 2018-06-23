package com.imooc.log

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * 使用Spark完成我们的数据清洗操作：运行在Yarn之上
  */
object SparkStatCleanJobYarn {

  def main(args: Array[String]): Unit = {


    if (args.length != 2) {
      println("Usage:SparkStatCleanJobYARN <inputPath> <outputPath>")
      System.exit(1)
    }

    val Array(inputPath, outputPath) = args

    val spark = SparkSession.builder().getOrCreate()

    val accessRDD = spark.sparkContext.textFile(inputPath)

    //accessRDD.take(10).foreach(println)

    //将RDD => DataFrame
    val accessDF = spark.createDataFrame(accessRDD.map(x => AccessConvertUtil.parseLog(x)), AccessConvertUtil.struct)

    //存储数据
    accessDF.coalesce(1).write.format("parquet").partitionBy("day").mode(SaveMode.Overwrite).save(outputPath)

    spark.stop()

  }

}
