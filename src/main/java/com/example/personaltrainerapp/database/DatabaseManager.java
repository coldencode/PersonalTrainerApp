package com.example.personaltrainerapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseManager {
    private Connection connection;
    private static final String URL = "jdbc:sqlite:personaltrainer.db";
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void getConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                logger.info("Connected to database");
            }
        } catch (SQLException e) {
            logger.info(e.toString());
            throw new RuntimeException("DB connection failed");
        }
    }
}
