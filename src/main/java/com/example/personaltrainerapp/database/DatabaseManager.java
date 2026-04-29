package com.example.personaltrainerapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:personaltrainer.db";
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Connection getConnection() {
        try {
//            if(connection == null || connection.isClosed()) {
            Connection connection = DriverManager.getConnection(URL);
                logger.info("Connected to database");
                return connection;
//            }

        } catch (SQLException e) {
            logger.info(e.toString());
            throw new RuntimeException("DB connection failed");
        }
    }
}
