package com.example.personaltrainerapp.repository;

import com.example.personaltrainerapp.enums.WorkoutType;
import com.example.personaltrainerapp.model.entries.WorkoutEntry;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkoutRepository {

    private final Connection connection;

    public WorkoutRepository(Connection connection) {
        this.connection = connection;
    }

    public void logWorkout(int userId, WorkoutType type, int durationMinutes, Double distanceKm, LocalDate date) {
        String sql = "INSERT INTO workout_log (user_id, type, duration_minutes, distance_km, date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, type.getLabel());
            ps.setInt(3, durationMinutes);
            if (distanceKm != null) ps.setDouble(4, distanceKm); else ps.setNull(4, Types.REAL);
            ps.setString(5, date.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB save failed", e);
        }
    }

    public List<WorkoutEntry> getRecentWorkouts(int userId) {
        String sql = "SELECT id, user_id, type, duration_minutes, distance_km, date FROM workout_log WHERE user_id = ? ORDER BY date DESC, id DESC LIMIT 20";
        return queryEntries(sql, userId);
    }

    public List<WorkoutEntry> getThisWeekWorkouts(int userId) {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate sunday = LocalDate.now().with(DayOfWeek.SUNDAY);
        String sql = "SELECT id, user_id, type, duration_minutes, distance_km, date FROM workout_log WHERE user_id = ? AND date >= ? AND date <= ? ORDER BY date DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, monday.toString());
            ps.setString(3, sunday.toString());
            return mapResultSet(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    public double getMaxRunDistanceKm(int userId) {
        String sql = "SELECT COALESCE(MAX(distance_km), 0) FROM workout_log WHERE user_id = ? AND type = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, WorkoutType.RUNNING.getLabel());
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    public double getBestRunPaceMinPerKm(int userId) {
        String sql = "SELECT MIN(CAST(duration_minutes AS REAL) / distance_km) FROM workout_log WHERE user_id = ? AND type = ? AND distance_km > 0";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, WorkoutType.RUNNING.getLabel());
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    public Optional<WorkoutEntry> getLongestWorkout(int userId) {
        String sql = "SELECT id, user_id, type, duration_minutes, distance_km, date FROM workout_log WHERE user_id = ? ORDER BY duration_minutes DESC LIMIT 1";
        List<WorkoutEntry> results = queryEntries(sql, userId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    // ── Helpers ──

    private List<WorkoutEntry> queryEntries(String sql, int userId) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return mapResultSet(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }

    private List<WorkoutEntry> mapResultSet(ResultSet rs) throws SQLException {
        List<WorkoutEntry> list = new ArrayList<>();
        while (rs.next()) {
            double distRaw = rs.getDouble("distance_km");
            Double dist = rs.wasNull() ? null : distRaw;
            list.add(new WorkoutEntry(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    WorkoutType.fromLabel(rs.getString("type")),
                    rs.getInt("duration_minutes"),
                    dist,
                    LocalDate.parse(rs.getString("date"))
            ));
        }
        return list;
    }
}
