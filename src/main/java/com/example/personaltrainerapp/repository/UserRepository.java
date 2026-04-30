package com.example.personaltrainerapp.repository;

import com.example.personaltrainerapp.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class UserRepository {

    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public Optional<User> findFirst() {
        String sql = "SELECT id, name, weight, height, goal, date_of_birth, gender, weekly_goal FROM users LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setWeight(rs.getDouble("weight"));
                user.setHeight(rs.getDouble("height"));
                user.setGoal(rs.getString("goal"));
                String dob = rs.getString("date_of_birth");
                if (dob != null) user.setDateOfBirth(LocalDate.parse(dob));
                user.setGender(rs.getString("gender"));
                user.setWeeklyGoal(rs.getString("weekly_goal"));
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
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
