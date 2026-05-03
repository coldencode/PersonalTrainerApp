package com.example.personaltrainerapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 *  Sets up the connection for the database
 */
public class DatabaseManager {
    /**
     * JDBC Connection URL for the SQLite Database
     */
    private static final String URL = "jdbc:sqlite:personaltrainer.db";
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Establishes and returns a new connection URL to the database.
     * @return Connection - Connection URL to the database
     */
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
