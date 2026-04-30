package com.example.personaltrainerapp.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSchema {
    public static void init(Connection conn) {
        createUserTable(conn);
        createWeightLogTable(conn);
        createMealLogTable(conn);
        createWorkoutLogTable(conn);
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

    private static void createWeightLogTable(Connection connection) {
        String weightLogTable = """
            CREATE TABLE IF NOT EXISTS weight_log (
                id      INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                weight  REAL    NOT NULL,
                date    TEXT    NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id)
            );
        """;
        executeStatement(connection, weightLogTable);
    }

    private static void createMealLogTable(Connection connection) {
        String mealLogTable = """
            CREATE TABLE IF NOT EXISTS meal_log (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id   INTEGER NOT NULL,
                meal_type TEXT    NOT NULL,
                calories  INTEGER NOT NULL,
                date      TEXT    NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id)
            );
        """;
        executeStatement(connection, mealLogTable);
    }

    private static void createWorkoutLogTable(Connection connection) {
        String workoutLogTable = """
            CREATE TABLE IF NOT EXISTS workout_log (
                id               INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id          INTEGER NOT NULL,
                type             TEXT    NOT NULL,
                duration_minutes INTEGER NOT NULL,
                distance_km      REAL,
                date             TEXT    NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id)
            );
        """;
        executeStatement(connection, workoutLogTable);
    }
}
