package com.example.personaltrainerapp.ui.dashboard;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.enums.MealType;
import com.example.personaltrainerapp.model.entries.DailyCalorieEntry;
import com.example.personaltrainerapp.model.entries.MealEntry;
import com.example.personaltrainerapp.model.User;
import com.example.personaltrainerapp.model.entries.WeightEntry;
import com.example.personaltrainerapp.repository.MealRepository;
import com.example.personaltrainerapp.repository.UserRepository;
import com.example.personaltrainerapp.repository.WeightRepository;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.Period;

public class DashboardViewModel {

    private final User user;
    private final ObservableList<WeightEntry> weightEntries = FXCollections.observableArrayList();
    private final ObservableList<MealEntry> todayMeals = FXCollections.observableArrayList();
    private final ObservableList<DailyCalorieEntry> calorieDailyTotals = FXCollections.observableArrayList();
    private final IntegerProperty todayIntake = new SimpleIntegerProperty(0);
    private int dailyCalories;
    private final WeightRepository weightRepo;
    private final MealRepository mealRepo;
    private final UserRepository userRepo;

    public DashboardViewModel() {
        this(new DatabaseManager().getConnection());
    }

    public DashboardViewModel(Connection conn) {
        this(new UserRepository(conn), new WeightRepository(conn), new MealRepository(conn));
    }

    public DashboardViewModel(UserRepository userRepo, WeightRepository weightRepo, MealRepository mealRepo) {
        this.userRepo   = userRepo;
        this.weightRepo = weightRepo;
        this.mealRepo   = mealRepo;

        user = userRepo.findFirst()
                       .orElseThrow(() -> new IllegalStateException("No user found"));

        refreshTodayIntake();

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

    // ── BMI calculation ──

    /** Returns BMI rounded to one decimal place, or -1 if data is missing. */
    public double getBmi() {
        if (user.getWeight() == 0 || user.getHeight() == 0) return -1;
        double heightM = user.getHeight() / 100.0;
        return Math.round((user.getWeight() / (heightM * heightM)) * 10.0) / 10.0;
    }

    /** WHO BMI category label. */
    public String getBmiCategory() {
        double bmi = getBmi();
        if (bmi < 0)    return "—";
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal weight";
        if (bmi < 30.0) return "Overweight";
        return "Obese";
    }

    // ── Weekly goal ──

    public void updateWeeklyGoal(String weeklyGoal) {
        user.setWeeklyGoal(weeklyGoal);
        userRepo.updateWeeklyGoal(user.getId(), weeklyGoal);
        dailyCalories = calculateDailyCalories(user);
    }

    // ── Meal log ──

    public void logMeal(MealType mealType, int calories) {
        mealRepo.logMeal(user.getId(), mealType, calories, LocalDate.now());
        refreshTodayIntake();
    }

    private void refreshTodayIntake() {
        todayMeals.setAll(mealRepo.getTodayMeals(user.getId(), LocalDate.now()));
        todayIntake.set(mealRepo.getTodayTotalCalories(user.getId(), LocalDate.now()));
        calorieDailyTotals.setAll(mealRepo.getDailyTotals(user.getId()));
    }

    public ObservableList<MealEntry> getTodayMeals()                      { return todayMeals; }
    public IntegerProperty todayIntakeProperty()                          { return todayIntake; }
    public ObservableList<DailyCalorieEntry> getCalorieDailyTotals()      { return calorieDailyTotals; }

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
