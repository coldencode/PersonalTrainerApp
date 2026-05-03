package com.example.personaltrainerapp.repository;

import com.example.personaltrainerapp.model.pushupbuddies.Brandon;
import com.example.personaltrainerapp.model.pushupbuddies.Friend;
import com.example.personaltrainerapp.model.pushupbuddies.Louis;
import com.example.personaltrainerapp.model.pushupbuddies.Simon;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

/**
 * Repository class responsible for managing Friends and handling
 * the daily push-up activity logic for a Friend
 */
public class FriendRepository {

    private final Connection conn;
    private final Random random = new Random();

    public FriendRepository(Connection conn) {
        this.conn = conn;
    }

    /** Returns true if the user has already locked in a friend choice. */
    public boolean hasFriend(int userId) {
        String sql = "SELECT COUNT(*) FROM friend_data WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("hasFriend failed", e);
        }
    }

    /**
     * Saves the friend selection for the user. Only calls once — the user
     * cannot change their choice afterwards.
     */
    public void selectFriend(int userId, String friendName) {
        String sql = """
            INSERT INTO friend_data (user_id, friend_name, total_pushups, last_updated_date)
            VALUES (?, ?, 0, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, friendName);
            ps.setString(3, LocalDate.now().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("selectFriend failed", e);
        }
    }

    /**
     * Loads the friend for the user, simulates any days that have passed
     * since the last update (each missed day has pushUpProbability chance
     * of adding dailyPushUps), persists the updated total, and returns the
     * Friend instance. Returns empty if no friend has been selected yet.
     */
    public Optional<Friend> loadAndSimulate(int userId) {
        System.out.println("[loadAndSimulate] Starting simulation for userId=" + userId);
        String sql = "SELECT friend_name, total_pushups, last_updated_date FROM friend_data WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                System.out.println("[loadAndSimulate] No friend found for userId=" + userId);
                return Optional.empty();
            }

            String friendName = rs.getString("friend_name");
            int storedTotal = rs.getInt("total_pushups");
            LocalDate lastUpdated = LocalDate.parse(rs.getString("last_updated_date"));

            System.out.println("[loadAndSimulate] Loaded friend: " + friendName
                    + ", storedTotal=" + storedTotal
                    + ", lastUpdated=" + lastUpdated);

            Friend friend = instantiateFriend(friendName);
            friend.setTotalPushUps(storedTotal);

            // Simulate every day from lastUpdated+1 up to and including today
            LocalDate today = LocalDate.now();
            long daysMissed = ChronoUnit.DAYS.between(lastUpdated, today);

            System.out.println("[loadAndSimulate] Days to simulate: " + daysMissed);

            if (daysMissed > 0) {
                int newTotal = storedTotal;
                for (long i = 0; i < daysMissed; i++) {
                    double roll = random.nextDouble();
                    boolean didPushUps = roll < friend.getPushUpProbability();
                    System.out.println("[loadAndSimulate] Day " + (i + 1) + "/" + daysMissed
                            + " - roll=" + String.format("%.3f", roll)
                            + ", probability=" + friend.getPushUpProbability()
                            + ", didPushUps=" + didPushUps);
                    if (didPushUps) {
                        newTotal += friend.getDailyPushUps();
                    }
                }
                System.out.println("[loadAndSimulate] Simulation complete. "
                        + friendName + " total: " + storedTotal + " -> " + newTotal);
                friend.setTotalPushUps(newTotal);
                persistUpdate(userId, newTotal, today);
            } else {
                System.out.println("[loadAndSimulate] No days missed, skipping simulation.");
            }

            return Optional.of(friend);

        } catch (SQLException e) {
            throw new RuntimeException("loadAndSimulate failed", e);
        }
    }

    /**
     * Update the friend's data with the new total pushups and updated date
     * @param userId - User ID
     * @param newTotal - New total push-ups counter
     * @param date - Date of the update
     */
    private void persistUpdate(int userId, int newTotal, LocalDate date) {
        String sql = "UPDATE friend_data SET total_pushups = ?, last_updated_date = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newTotal);
            ps.setString(2, date.toString());
            ps.setInt(3, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("persistUpdate failed", e);
        }
    }

    /**
     * To instantiate which friend
     * @param name - Name of the Friend
     * @return Friend class that represents that friend
     */
    private Friend instantiateFriend(String name) {
        return switch (name) {
            case "Simon"   -> new Simon();
            case "Brandon" -> new Brandon();
            default        -> new Louis();
        };
    }
}
