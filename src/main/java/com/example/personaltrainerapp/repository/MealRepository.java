package com.example.personaltrainerapp.repository;

import com.example.personaltrainerapp.model.DailyCalorieEntry;
import com.example.personaltrainerapp.model.MealEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealRepository {

    private final Connection connection;

    public MealRepository(Connection connection) {
        this.connection = connection;
    }

    public void logMeal(int userId, String mealType, int calories, LocalDate date) {
        String sql = "INSERT INTO meal_log (user_id, meal_type, calories, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, mealType);
            ps.setInt(3, calories);
            ps.setString(4, date.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB save failed", e);
        }
    }

    public List<MealEntry> getTodayMeals(int userId, LocalDate date) {
        String sql = "SELECT id, user_id, meal_type, calories, date FROM meal_log WHERE user_id = ? AND date = ? ORDER BY id ASC";
        List<MealEntry> entries = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, date.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entries.add(new MealEntry(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("meal_type"),
                        rs.getInt("calories"),
                        LocalDate.parse(rs.getString("date"))
                ));
            }
            return entries;
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    /** Returns one entry per day that has at least one meal logged, ordered oldest → newest. */
    public List<DailyCalorieEntry> getDailyTotals(int userId) {
        String sql = """
            SELECT date, SUM(calories) AS total
            FROM meal_log
            WHERE user_id = ?
            GROUP BY date
            ORDER BY date ASC
        """;
        List<DailyCalorieEntry> entries = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entries.add(new DailyCalorieEntry(
                        LocalDate.parse(rs.getString("date")),
                        rs.getInt("total")
                ));
            }
            return entries;
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    public int getTodayTotalCalories(int userId, LocalDate date) {
        String sql = "SELECT COALESCE(SUM(calories), 0) FROM meal_log WHERE user_id = ? AND date = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, date.toString());
            ResultSet rs = ps.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }
}
