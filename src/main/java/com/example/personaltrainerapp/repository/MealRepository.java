package com.example.personaltrainerapp.repository;

import com.example.personaltrainerapp.enums.MealType;
import com.example.personaltrainerapp.model.entries.DailyCalorieEntry;
import com.example.personaltrainerapp.model.entries.MealEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class to handle all the SQL logic for Meals
 */
public class MealRepository {

    private final Connection connection;

    public MealRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * To log the Meal recorded by the User
     * @param userId - User ID that logged the meal
     * @param mealType - Type of meal that was logged
     * @param calories - Calories recorded
     * @param date - Date recorded
     */
    public void logMeal(int userId, MealType mealType, int calories, LocalDate date) {
        String sql = "INSERT INTO meal_log (user_id, meal_type, calories, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, mealType.getLabel());
            ps.setInt(3, calories);
            ps.setString(4, date.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB save failed", e);
        }
    }

    /**
     * Method to query the database to get the list of meal entries from today only
     * @param userId - User ID
     * @param date - Today's date
     * @return a list of meal entries from today
     */
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
                        MealType.fromLabel(rs.getString("meal_type")),
                        rs.getInt("calories"),
                        LocalDate.parse(rs.getString("date"))
                ));
            }
            return entries;
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    /**
     * Returns the sum of calories per day that has at least one meal logged, ordered oldest to newest.
     * @param userId - User ID
     * @return a list of daily calory entries
     */
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

    /**
     * Return the sum of calories from today
     * @param userId - User ID
     * @param date - Today's date
     * @return an integer representing the number of calories consumed today
     */
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
