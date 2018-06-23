package com.boy.spark

import org.apache.spark.sql.SparkSession

object SchemaInferApp {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("").master("local[2]").getOrCreate()

    val df = spark.read.format("json").load("src/main/data/json_schema_infer.json")

    df.printSchema()

    df.show()


    spark.close()
  }
}
