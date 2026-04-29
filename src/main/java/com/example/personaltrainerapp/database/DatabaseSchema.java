package com.example.personaltrainerapp.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSchema {
    public static void init(Connection conn) {
        // Create a User Table
        createUserTable(conn);

    }

    /**
     * Helper function that executes a SQL query
     * @param connection - Connection to the database
     * @param query - Query in the string that is executed
     */
    private static void executeStatement(Connection connection, String query) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }

    /**
     * Creates a User Table if it does not already exist
     */
    private static void createUserTable(Connection connection) {
        // SQL Query to create the User Table (if it does not exist)
        String usersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                weight REAL,
                height REAL,
                goal TEXT,
                date_of_birth TEXT,
                gender TEXT,
                weekly_goal TEXT
            );
        """;
        executeStatement(connection, usersTable);

    }
}
