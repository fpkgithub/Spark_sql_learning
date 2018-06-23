package com.imooc.log

import com.ggstar.util.ip.IpHelper

/**
  * IP解析工具类
  */
object IpUtils {


  def getCity(ip: String) = {
    IpHelper.findRegionByIp(ip)
  }

  def main(args: Array[String]): Unit = {
    println(getCity("183.162.52.7"))
  }

}
