package com.example.personaltrainerapp.repository;

import com.example.personaltrainerapp.model.entries.WeightEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A repository class to handle all weight records
 */
public class WeightRepository {

    private final Connection connection;

    public WeightRepository(Connection connection) {
        this.connection = connection;
    }

    public void addEntry(int userId, double weight, LocalDate date) {
        String sql = "INSERT INTO weight_log (user_id, weight, date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDouble(2, weight);
            ps.setString(3, date.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB save failed", e);
        }
    }

    /**
     * Get the list of Weight entries from the DB
     * @param userId - User ID
     * @return a list of weight entry record instances
     */
    public List<WeightEntry> getEntries(int userId) {
        String sql = "SELECT id, weight, date FROM weight_log WHERE user_id = ? ORDER BY date ASC";
        List<WeightEntry> entries = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entries.add(new WeightEntry(
                        rs.getInt("id"),
                        rs.getDouble("weight"),
                        LocalDate.parse(rs.getString("date"))
                ));
            }
            return entries;
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed", e);
        }
    }
}
