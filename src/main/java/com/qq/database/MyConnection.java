package com.qq.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    static String url = "jdbc:mysql://localhost:3306/oicq?useUnicode=true&amp&characterEncoding=utf8";//数据库

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个数据库连接
     * @return null：数据库连接获取失败；一个数据库连接
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, "root", "Yz946863123");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
