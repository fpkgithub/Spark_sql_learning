package com.imooc.log

import java.sql.{Connection, PreparedStatement}

import scala.collection.mutable.ListBuffer

/**
  * 各个维度统计的DAO操作
  */
object StatDAO {


  /**
    * 批量保存DayVideoAccessStat到数据库
    *
    * @param list
    */
  def insertDayVideoAccessTopN(list: ListBuffer[DayVideoAccessStat]): Unit = {

    var connection: Connection = null
    var pstmt: PreparedStatement = null

    try {

      connection = MySQLUtils.getConnection()

      //使用批处理 设置手动提交
      connection.setAutoCommit(false)

      val sql = "insert into day_video_access_topn_stat(day,cms_id,times) values(?,?,?)"

      pstmt = connection.prepareStatement(sql)

      for (ele <- list) {
        pstmt.setString(1, ele.day)
        pstmt.setLong(2, ele.cmsId)
        pstmt.setLong(3, ele.times)

        pstmt.addBatch()

      }
      //执行批量处理
      pstmt.executeBatch()
      //手动提交
      connection.commit()
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
    finally {

    }

  }

  /**
    * 批量保存DayCityVideoAccessStat到数据库
    *
    * @param list
    */
  def insertDayCityVideoAccessTopN(list: ListBuffer[DayCityViedoAccessStat]): Unit = {

    var connection: Connection = null
    var pstmt: PreparedStatement = null

    try {

      connection = MySQLUtils.getConnection()

      //使用批处理 设置手动提交
      connection.setAutoCommit(false)

      val sql = "insert into day_video_city_access_topn_stat(day,cms_id,city,times,times_rank) values(?,?,?,?,?)"

      pstmt = connection.prepareStatement(sql)

      for (ele <- list) {
        pstmt.setString(1, ele.day)
        pstmt.setLong(2, ele.cmsId)
        pstmt.setString(3, ele.city)
        pstmt.setLong(4, ele.times)
        pstmt.setInt(5, ele.timesRank)
        pstmt.addBatch()
      }
      //执行批量处理
      pstmt.executeBatch()
      //手动提交
      connection.commit()
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
    finally {

    }

  }

  /**
    * 批量保存DayVideoTrafficsStat到数据库
    *
    * @param list
    */
  def insertDayVideoTrafficsAccessTopN(list: ListBuffer[DayVideoTrafficsStat]): Unit = {

    var connection: Connection = null
    var pstmt: PreparedStatement = null

    try {

      connection = MySQLUtils.getConnection()

      //使用批处理 设置手动提交
      connection.setAutoCommit(false)

      val sql = "insert into day_video_traffics_topn_stat(day,cms_id,traffics) values(?,?,?)"

      pstmt = connection.prepareStatement(sql)

      for (ele <- list) {
        pstmt.setString(1, ele.day)
        pstmt.setLong(2, ele.cmsId)
        pstmt.setLong(3, ele.traffics)
        pstmt.addBatch()
      }
      //执行批量处理
      pstmt.executeBatch()
      //手动提交
      connection.commit()
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
    finally {

    }

  }

  /**
    * 删除指定日期的数据
    */
  def deleteData(day: String): Unit = {

    val tables = Array("day_video_access_topn_stat", "day_video_city_access_topn_stat", "day_video_traffics_topn_stat")

    var connection: Connection = null
    var pstmt: PreparedStatement = null

    try {
      connection = MySQLUtils.getConnection()
      for (table <- tables) {
        //delete from table ....
        val deleteSQL = s"delete from $table where day = ?"
        pstmt = connection.prepareStatement(deleteSQL)
        pstmt.setString(1, day)
        pstmt.executeUpdate()
      }
    }
    catch {
      case e: Exception => e.printStackTrace()
    }


  }

}
