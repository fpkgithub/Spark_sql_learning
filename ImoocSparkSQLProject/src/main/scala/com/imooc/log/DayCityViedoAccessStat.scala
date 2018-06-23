package com.imooc.log

/**
  * 每天按照城市统计最受欢迎的TopN课程
  *
  * @param day
  * @param cmsId
  * @param city
  * @param timesRank
  */
case class DayCityViedoAccessStat(day: String, cmsId: Long, city: String, times: Long, timesRank: Int)
