package com.imooc.utils;

import java.sql.*;

/**
 * 操作MySQL的工具类
 */
public class MySQLUtils
{
    private static final String USERNAME = "root";

    private static final String PASSWORD = "root";

    private static final String DRIVERCLASS = "com.mysql.jdbc.Driver";

    private static final String URL = "jdbc:mysql://192.168.95.128:3306/imooc_project";


    /**
     * 建立连接
     *
     * @return
     */
    public static Connection getConnection()
    {

        Connection connection = null;

        try
        {
            Class.forName(DRIVERCLASS);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 释放资源
     *
     * @param connection
     * @param preparedStatement
     * @param resultSet
     */
    public static void release(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet)
    {
        if (resultSet != null)
        {
            try
            {
                resultSet.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        if (preparedStatement != null)
        {
            try
            {
                preparedStatement.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        if (connection != null)
        {
            try
            {
                connection.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args)
    {
        System.out.println(getConnection());
    }

}
