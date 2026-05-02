package com.example.personaltrainerapp.repository;

import com.example.personaltrainerapp.model.entries.PushUpEntry;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PushUpRepository {

    private final Connection connection;

    public PushUpRepository(Connection connection) {
        this.connection = connection;
    }

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

    public List<PushUpEntry> getRecentEntries(int userId) {
        String sql = "SELECT id, user_id, count, date FROM pushup_log WHERE user_id = ? ORDER BY date DESC, id DESC LIMIT 20";
        return queryEntries(sql, userId);
    }

    /** All entries ordered oldest → newest, used to build the chart. */
    public List<PushUpEntry> getAllEntries(int userId) {
        String sql = "SELECT id, user_id, count, date FROM pushup_log WHERE user_id = ? ORDER BY date ASC, id ASC";
        return queryEntries(sql, userId);
    }

    public int getOverallTotal(int userId) {
        return sumWhere(userId, "1=1");
    }

    public int getWeeklyTotal(int userId) {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate sunday = LocalDate.now().with(DayOfWeek.SUNDAY);
        String sql = "SELECT COALESCE(SUM(count), 0) FROM pushup_log WHERE user_id = ? AND date >= ? AND date <= ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, monday.toString());
            ps.setString(3, sunday.toString());
            return ps.executeQuery().getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    public int getMonthlyTotal(int userId) {
        LocalDate firstOfMonth = LocalDate.now().withDayOfMonth(1);
        String sql = "SELECT COALESCE(SUM(count), 0) FROM pushup_log WHERE user_id = ? AND date >= ? AND date <= ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, firstOfMonth.toString());
            ps.setString(3, LocalDate.now().toString());
            return ps.executeQuery().getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    // ── Helpers ──

    private int sumWhere(int userId, String condition) {
        String sql = "SELECT COALESCE(SUM(count), 0) FROM pushup_log WHERE user_id = ? AND " + condition;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeQuery().getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

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
