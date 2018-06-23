package com.imooc.dao;

import com.imooc.domain.VideoAccessTopN;
import com.imooc.utils.MySQLUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 面向接口编程
 */
public class VideoAccessTopNDAO
{

    static Map<String, String> courses = new HashMap<String, String>();

    static
    {
        courses.put("4000", "MySQL优化");
        courses.put("4500", "Crontab");
        courses.put("4600", "Swift");
        courses.put("14540", "SpringData");
        courses.put("14704", "R");
        courses.put("14390", "机器学习");
        courses.put("14322", "redis");
        courses.put("14390", "神经网络");
        courses.put("14623", "Docker");
    }


    /**
     * 根据课程编号查询课程名称
     *
     * @param id
     * @return
     */
    public String getCourseName(String id)
    {
        return courses.get(id);
    }


    /**
     * 根据day查询当天的最受欢迎的Top5课程
     *
     * @param day
     * @return
     */
    public List<VideoAccessTopN> query(String day)
    {

        List<VideoAccessTopN> list = new ArrayList<VideoAccessTopN>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try
        {
            connection = MySQLUtils.getConnection();
            String sql = "select cms_id,times from day_video_access_topn_stat where day = ? order by times DESC limit 15";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, day);
            resultSet = preparedStatement.executeQuery();
            VideoAccessTopN domain = null;
            while (resultSet.next())
            {
                domain = new VideoAccessTopN();
                /**
                 * 根据课程编号获取课程名称
                 */
                domain.setName(getCourseName(resultSet.getLong("cms_Id") + ""));

                domain.setValue(resultSet.getLong("times"));
                list.add(domain);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            MySQLUtils.release(connection, preparedStatement, resultSet);
        }
        return list;
    }

    public static void main(String[] args)
    {
        VideoAccessTopNDAO dao = new VideoAccessTopNDAO();
        List<VideoAccessTopN> list = dao.query("20170511");
        for (VideoAccessTopN val : list)
        {
            System.out.println(val.getName() + " " + val.getValue());
        }
    }

}
