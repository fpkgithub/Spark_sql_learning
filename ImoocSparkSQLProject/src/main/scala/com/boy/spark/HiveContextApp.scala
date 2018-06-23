package com.boy.spark

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * HiveContext的使用
  * --jars传递mysql驱动
  */
object HiveContextApp {

  def main(args: Array[String]): Unit = {

    //1) 创建相应的Context
    val sparkConf = new SparkConf()
    //在测试或者生产中，AppBName和Master我们是通过脚本进行指定
    //sparkConf.setAppName("HiveContextApp").setMaster("local[2]")

    val sc = new SparkContext(sparkConf)
    val hiveContext = new HiveContext(sc)

    //2) 相关的处理：josn
    hiveContext.table("emp").show

    //3) 关于资源
    sc.stop()
  }

}
