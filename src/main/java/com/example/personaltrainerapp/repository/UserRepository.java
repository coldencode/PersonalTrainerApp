package com.example.personaltrainerapp.repository;

import com.example.personaltrainerapp.model.User;

import java.sql.*;

public class UserRepository {

    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public boolean hasUser() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    public void save(User user) {
        String sql = """
            INSERT INTO users (name, weight, height, goal, date_of_birth, gender, weekly_goal)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setDouble(2, user.getWeight());
            ps.setDouble(3, user.getHeight());
            ps.setString(4, user.getGoal());
            ps.setString(5, user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : null);
            ps.setString(6, user.getGender());
            ps.setString(7, user.getWeeklyGoal());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB save failed", e);
        }
    }
}
