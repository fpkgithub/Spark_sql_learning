package com.boy.spark

import org.apache.spark.sql.SparkSession

/**
  * DataFrame中的操作
  */
object DataFrameCase {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataFrameCase").master("local[2]").getOrCreate()

    val rdd = spark.sparkContext.textFile("src/main/data/student.data")

    import spark.implicits._
    val studentDF = rdd.map(_.split("\\|")).map(line => Student(line(0).toInt, line(1), line(2), line(3))).toDF()

    studentDF.show(30, false)
    //前10条
    studentDF.take(10).foreach(println)
    //第一条记录
    studentDF.first()
    //前三条
    studentDF.head(3)
    //指定的列
    studentDF.select("name", "email").show(30, false)
    //过滤name=''
    studentDF.filter("name='' OR name='NULL'").show()

    //name以M开头的人
    studentDF.filter("SUBSTR(name,0,1)='M'").show()
    //查询内置函数
    spark.sql("show functions").show(1000)

    // //name以M开头的人
    studentDF.filter("substring(name,0,1)='M'").show()

    //排序
    studentDF.sort(studentDF("name")).show()
    studentDF.sort(studentDF("name").desc).show()
    studentDF.sort("name", "id").show()
    studentDF.sort(studentDF("name").asc, studentDF("id").desc).show()

    //重命名
    studentDF.select(studentDF("name").as("student_name")).show()

    //join操作
    val studentDF2 = rdd.map(_.split("\\|")).map(line => Student(line(0).toInt, line(1), line(2), line(3))).toDF()
    studentDF.join(studentDF2, studentDF.col("id") === studentDF2.col("id")).show()

    spark.stop()
  }

  case class Student(id: Int, name: String, phone: String, email: String)

}
