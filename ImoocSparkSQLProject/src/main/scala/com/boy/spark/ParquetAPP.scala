package com.boy.spark

import org.apache.spark.sql.SparkSession

/**
  * Parquet文件操作
  */
object ParquetAPP {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("ParquetAPP").master("local[2]").getOrCreate()

    /**
      * spark.read.format("parquet").load这是标准写法
      */
    val userDF = spark.read.format("parquet").load("src/main/data/users.parquet")

    userDF.printSchema()
    userDF.show()

    userDF.select("name","favorite_color").show()
    userDF.select("name","favorite_color").write.format("json").save("src/main/data/jsonout")

    //也会显示
    spark.read.format("parquet").load("src/main/data/users.parquet").show()

    //会报错，因为sparksql默认处理的format就是parquet
    spark.read.format("parquet").load("src/main/data/people.json").show()

    spark.sqlContext.setConf("spark.sql.shuffle.partitions","10")

    spark.read.format("parquet").option("path","src/main/data/users.parquet").load().show()


    spark.stop()
  }
}
