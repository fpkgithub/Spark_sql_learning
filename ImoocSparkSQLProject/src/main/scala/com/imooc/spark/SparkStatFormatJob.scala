package com.imooc.spark

import org.apache.spark.sql.SparkSession

/**
  * 第一步清洗：抽取出我们需要的指定列的数据
  */
object SparkStatFormatJob {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("SparkStatFormatJob").master("local[2]").getOrCreate()

    val access = spark.sparkContext.textFile("src/main/data/10000_access.log")

    //access.take(10).foreach(println)

    access.map(line => {
      val splits = line.split(" ")
      val ip = splits(0)

      /**
        * 原始日志的第三个个第四个字段拼接起来就是完整的访问时间：
        * [10/Nov/2016:00:01:02 +0800] ===> yyyy-MM-dd HH:mm:ss
        */
      val time = splits(3) + " " + splits(4)
      val url = splits(11).replace("\"", "")
      val traffic = splits(9)

      //(ip, DateUtils.parse(time), url, traffic)

      DateUtils.parse(time) + "\t" + url + "\t" + traffic + "\t" + ip

    }).saveAsTextFile("src/main/data/output/")
    //spark的saveAsTextFile方法只能指定文件夹，但是保存到本地的话，会报空指针错误。
    //参考：https://blog.csdn.net/kimyoungvon/article/details/51308651
    spark.stop()
  }

}
