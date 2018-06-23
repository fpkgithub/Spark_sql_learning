package com.imooc.spark

import java.util.{Date, Locale}

import org.apache.commons.lang3.time.FastDateFormat

/**
  * 日期时间解析工具类
  * 注意：SimpleDateFormat是线程不安全
  */
object DateUtils {

  //注意年份日期时间的格式一定要正确的大小写

  //输入文件日期时间格式：10/Nov/2016:00:01:02 +0800
  //val YYYYMMDDHHMM_TIME_FORMAT = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)
  val YYYYMMDDHHMM_TIME_FORMAT = FastDateFormat.getInstance("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)

  //目标日期格式
  //yyyy-MM-dd HH:mm:ss
  //val TARGET_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val TARGET_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")


  /**
    * 获取时间：yyyy-MM-dd HH:mm:ss
    *
    * @param time
    */
  def parse(time: String) = {
    TARGET_FORMAT.format(new Date(getTime(time)))
  }

  /**
    * 获取输入日志时间：long类型
    *
    * @param time [10/Nov/2016:00:01:02 +0800]
    * @return
    */
  def getTime(time: String) = {
    try {
      YYYYMMDDHHMM_TIME_FORMAT.parse(time.substring(time.indexOf("[") + 1, time.lastIndexOf("]"))).getTime
    }
    catch {
      case e: Exception => {
        0l
      }
    }
  }


  def main(args: Array[String]): Unit = {
    println(parse("[10/Nov/2016:00:01:02 +0800]"))
  }


}
