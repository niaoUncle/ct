package com.china.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public class JDBCUtil {

    private static final Logger logger = LoggerFactory.getLogger(JDBCUtil.class);
    private static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String MYSQL_URL = "jdbc:mysql://hadoop102:3306/db_telecom?useUnicode=true&characterEncoding=UTF-8";
    private static final String MYSQL_USERNAME = "root";
    private static final String MYSQL_PASSWORD = "jimm";
    /**
     * 获取Mysql数据库的连接
     * @return
     */
    public static Connection getConnection() throws SQLException {

        try {
            //采用反射获取DRIVER驱动
            Class.forName(MYSQL_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //使用的驱动管理 //
        //java.sql.SQLException: Access denied for user 'com.mysql.jdbc.Driver'@'hadoop102' (using password: YES)
        //原因是没有使用用户名，用户名使用成了驱动
        return DriverManager.getConnection(MYSQL_URL,MYSQL_USERNAME,MYSQL_PASSWORD);
    }

    /**
     * 关闭数据库连接释放资源
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet){

        try {
            if(connection != null){
                connection.close();
            }
            if(statement != null){
                statement.close();
            }
            if(resultSet != null){
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
