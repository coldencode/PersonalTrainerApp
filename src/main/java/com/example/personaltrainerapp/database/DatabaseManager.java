package com.example.personaltrainerapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:fitness.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException("DB connection failed");
        }
    }
}
