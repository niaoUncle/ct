package com.china.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCSingleson {
    private static Connection conn = null;

    private JDBCSingleson() {
    }

    public static Connection getConn() {
        try {
            conn = JDBCUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
