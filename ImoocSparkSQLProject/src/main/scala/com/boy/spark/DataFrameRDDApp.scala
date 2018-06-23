package com.boy.spark

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
  * DataFrame和RDD的互操作
  * 两种编程方式：
  * a:基于DataFrame API
  * b:基于sql API
  */
object DataFrameRDDApp {


  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameRDDApp").master("local[2]").getOrCreate()

    //第一种方式：反射
    //inferReflection(spark)

    /** 第二种方式：编程
      * Create an RDD of Rows from the original RDD;
      * Create the schema represented by a StructType matching the structure of Rows in the RDD created in Step 1;
      * Apply the schema to the RDD of Rows via createDataFrame method provided by SparkSession.
      */
    program(spark)

    spark.stop()
  }

  //相当于一个java bean
  case class info(id: Int, name: String, age: Int)

  def program(spark: SparkSession): Unit = {
    //RDD ==> DataFrame
    val rdd = spark.sparkContext.textFile("src/main/data/infos.txt")

    //定义RDD
    val infoRDD = rdd.map(_.split(",")).map(line => Row(line(0).toInt, line(1), line(2).toInt))

    //定义结构
    val structType = StructType(Array(StructField("id", IntegerType, true),
      StructField("name", StringType, true),
      StructField("age", IntegerType, true)))

    val infoDF = spark.createDataFrame(infoRDD, structType)

    infoDF.printSchema()
    infoDF.show()

    //通过df的api进行操作
    infoDF.filter(infoDF.col("age") > 30).show()

    //通过sql的方式进行操作
    infoDF.createOrReplaceTempView("infos")
    spark.sql("select * from infos where age > 30").show()
  }


  private def inferReflection(spark: SparkSession) = {
    //RDD ==> DataFrame
    val rdd = spark.sparkContext.textFile("src/main/data/infos.txt")

    //注意：需要导入隐式转换
    import spark.implicits._
    val infoDF = rdd.map(_.split(",")).map(line => info(line(0).toInt, line(1), line(2).toInt)).toDF()

    infoDF.show()

    infoDF.filter(infoDF.col("age") > 30).show()

    infoDF.createOrReplaceTempView("infos")

    spark.sql("select * from infos where age > 30").show()
  }

}
