package com.boy.spark

import java.io.File

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * SQLContext的使用  
  * 注意：IDEA是在本地，而测试数据是在服务器上 ，能不能在本地进行开发测试的？
  */
object SQLContextApp {

  def main(args: Array[String]): Unit = {

    //设置Edit Configurations program arguments "src\main\data\people.json"
    val path = args(0)

    //1) 创建相应的Context
    val sparkConf = new SparkConf()
    //在测试或者生产中，AppBName和Master我们是通过脚本进行指定
    //sparkConf.setAppName("SQLContextApp").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sc)

    //2) 相关的处理：josn
    val people = sqlContext.read.format("json").load(path)
    people.printSchema()
    people.show()

    people.write.format("parquet").save("path")

    //3) 关于资源
    sc.stop()

  }

}
