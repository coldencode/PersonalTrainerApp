package com.example.personaltrainerapp.repository;

import com.example.personaltrainerapp.model.entries.PushUpEntry;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class to handle all the SQL logic for Push Ups
 */
public class PushUpRepository {

    private final Connection connection;

    public PushUpRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Log the push-up
     * @param userId - user ID
     * @param count - Number of reps
     * @param date - Date of entry
     */
    public void logPushUps(int userId, int count, LocalDate date) {
        String sql = "INSERT INTO pushup_log (user_id, count, date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, count);
            ps.setString(3, date.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB save failed", e);
        }
    }

    /**
     * Retrieve the latest 20 pushup entries from a User
     * @param userId - user ID
     * @return list of push up entries
     */
    public List<PushUpEntry> getRecentEntries(int userId) {
        String sql = "SELECT id, user_id, count, date FROM pushup_log WHERE user_id = ? ORDER BY date DESC, id DESC LIMIT 20";
        return queryEntries(sql, userId);
    }

    /**
     *  All push up entries ordered by date ascending, used to build the chart.
     * @param userId - user ID
     * @return a sorted list of push up entries
     */
    public List<PushUpEntry> getAllEntries(int userId) {
        String sql = "SELECT id, user_id, count, date FROM pushup_log WHERE user_id = ? ORDER BY date ASC, id ASC";
        return queryEntries(sql, userId);
    }

    /**
     * Get the total number of push-ups overall
     * @param userId - user ID
     * @return int representing the number of push-ups
     */
    public int getOverallTotal(int userId) {
        return querySum(userId,
                "SELECT COALESCE(SUM(count), 0) FROM pushup_log WHERE user_id = ?");
    }

    /**
     * Get the total number of push-ups this week
     * @param userId - user ID
     * @return int representing the number of push-ups
     */
    public int getWeeklyTotal(int userId) {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate sunday = LocalDate.now().with(DayOfWeek.SUNDAY);
        return querySum(userId,
                "SELECT COALESCE(SUM(count), 0) FROM pushup_log WHERE user_id = ? AND date >= ? AND date <= ?",
                monday.toString(), sunday.toString());
    }

    /**
     * Get the total number of push-ups this month
     * @param userId - user ID
     * @return int representing the number of push-ups
     */
    public int getMonthlyTotal(int userId) {
        LocalDate firstOfMonth = LocalDate.now().withDayOfMonth(1);
        return querySum(userId,
                "SELECT COALESCE(SUM(count), 0) FROM pushup_log WHERE user_id = ? AND date >= ? AND date <= ?",
                firstOfMonth.toString(), LocalDate.now().toString());
    }

    /**
     * Helper function to build the SQL query to get specific date ranges for push-ups
     * @param userId - user ID
     * @param sql - sql query in String
     * @param dateParams - date range parameters
     * @return the number of push-ups
     */
    private int querySum(int userId, String sql, String... dateParams) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            for (int i = 0; i < dateParams.length; i++) {
                ps.setString(i + 2, dateParams[i]);
            }
            return ps.executeQuery().getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    /**
     * Get a list of push-up entries from the SQLIte database to instantiate
     * as record instances
     * @param sql - SQL Query
     * @param userId - user ID
     * @return a list of push up entry records
     */
    private List<PushUpEntry> queryEntries(String sql, int userId) {
        List<PushUpEntry> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PushUpEntry(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("count"),
                        LocalDate.parse(rs.getString("date"))
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }
}
