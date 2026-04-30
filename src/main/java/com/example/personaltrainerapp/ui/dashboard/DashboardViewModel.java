package com.example.personaltrainerapp.ui.dashboard;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.model.User;
import com.example.personaltrainerapp.model.WeightEntry;
import com.example.personaltrainerapp.repository.UserRepository;
import com.example.personaltrainerapp.repository.WeightRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.Period;

public class DashboardViewModel {

    private final User user;
    private final ObservableList<WeightEntry> weightEntries = FXCollections.observableArrayList();
    private final int dailyCalories;
    private final WeightRepository weightRepo;

    public DashboardViewModel() {
        Connection conn = new DatabaseManager().getConnection();

        user = new UserRepository(conn)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No user found"));

        weightRepo = new WeightRepository(conn);

        // Seed the chart with the starting weight from onboarding if nothing logged yet
        weightEntries.setAll(weightRepo.getEntries(user.getId()));
        if (weightEntries.isEmpty() && user.getWeight() > 0) {
            weightRepo.addEntry(user.getId(), user.getWeight(), LocalDate.now());
            weightEntries.setAll(weightRepo.getEntries(user.getId()));
        }

        dailyCalories = calculateDailyCalories(user);
    }

    // ── Calorie calculation (Mifflin-St Jeor + TDEE + weekly goal adjustment) ──

    private int calculateDailyCalories(User user) {
        if (user.getWeight() == 0 || user.getHeight() == 0 || user.getDateOfBirth() == null) {
            return 2000; // sensible default when data is missing
        }

        int age = Period.between(user.getDateOfBirth(), LocalDate.now()).getYears();

        double bmr;
        if ("Male".equalsIgnoreCase(user.getGender())) {
            bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * age + 5;
        } else if ("Female".equalsIgnoreCase(user.getGender())) {
            bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * age - 161;
        } else {
            bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * age - 78; // midpoint
        }

        double tdee = bmr * 1.375; // lightly active (default until activity level is added)

        // Parse the weekly goal label set during onboarding, e.g. "Gain 0.5 kg / week"
        String weekly = user.getWeeklyGoal() != null ? user.getWeeklyGoal() : "";
        int adjustment = 0;
        if      (weekly.contains("0.2")) adjustment = weekly.startsWith("Gain") ? +220  : weekly.startsWith("Lose") ? -220  : 0;
        else if (weekly.contains("0.5")) adjustment = weekly.startsWith("Gain") ? +550  : weekly.startsWith("Lose") ? -550  : 0;
        else if (weekly.contains("1.0")) adjustment = weekly.startsWith("Gain") ? +1100 : weekly.startsWith("Lose") ? -1100 : 0;

        return (int) Math.round(tdee + adjustment);
    }

    // ── Weight log mutations ──

    public void addWeight(double weight) {
        weightRepo.addEntry(user.getId(), weight, LocalDate.now());
        weightEntries.setAll(weightRepo.getEntries(user.getId()));
    }

    // ── Getters ──

    public User getUser()                              { return user; }
    public ObservableList<WeightEntry> getWeightEntries() { return weightEntries; }
    public int getDailyCalories()                      { return dailyCalories; }
}
