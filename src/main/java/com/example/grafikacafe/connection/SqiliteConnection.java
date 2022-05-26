package com.example.grafikacafe.connection;
import java.sql.*;

public class SqiliteConnection {

    public static Connection Connector() {
    try {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:grafikacafe.db");
        return connection;
    } catch (Exception e) {
        System.out.println(e.getMessage());
        return null;
    }
    }
}
