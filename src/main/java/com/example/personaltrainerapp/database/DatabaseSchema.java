package com.example.personaltrainerapp.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Schema for the SQLite Database
 * Creates all the Tables if it does not already exist
 */
public class DatabaseSchema {
    /**
     * Static method to create all the tables in the database
     * @param conn - Connection URL to the database
     */
    public static void init(Connection conn) {
        // Creates the tables
        createUserTable(conn);
        createWeightLogTable(conn);
        createMealLogTable(conn);
        createWorkoutLogTable(conn);
        createPushUpLogTable(conn);
        createFriendDataTable(conn);
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

    /**
     * Creates a Weight Log table to log the weight history of a User
     */
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

    /**
     * Creates a Meal Log table to log the meal history of a User
     */
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

    /**
     * Create a Workout Log table to log the workout history of a User
     */
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

    /**
     * Creates a Push Up Log table to log the number of pushups by a User
     */
    private static void createPushUpLogTable(Connection connection) {
        String pushUpLogTable = """
            CREATE TABLE IF NOT EXISTS pushup_log (
                id      INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                count   INTEGER NOT NULL,
                date    TEXT    NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id)
            );
        """;
        executeStatement(connection, pushUpLogTable);
    }

    /**
     * Creates a Friend table to log daily pushups of a Friend
     */
    private static void createFriendDataTable(Connection connection) {
        String friendDataTable = """
            CREATE TABLE IF NOT EXISTS friend_data (
                id                INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id           INTEGER NOT NULL UNIQUE,
                friend_name       TEXT    NOT NULL,
                total_pushups     INTEGER NOT NULL DEFAULT 0,
                last_updated_date TEXT    NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id)
            );
        """;
        executeStatement(connection, friendDataTable);
    }
}
