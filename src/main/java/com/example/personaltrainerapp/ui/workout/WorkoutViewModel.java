package com.example.personaltrainerapp.ui.workout;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.enums.WorkoutType;
import com.example.personaltrainerapp.model.User;
import com.example.personaltrainerapp.model.entries.WorkoutEntry;
import com.example.personaltrainerapp.repository.UserRepository;
import com.example.personaltrainerapp.repository.WorkoutRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Optional;

public class WorkoutViewModel {

    private final User user;
    private final WorkoutRepository workoutRepo;
    private final ObservableList<WorkoutEntry> recentWorkouts = FXCollections.observableArrayList();

    public WorkoutViewModel() {
        this(new DatabaseManager().getConnection());
    }

    public WorkoutViewModel(Connection conn) {
        this(new UserRepository(conn), new WorkoutRepository(conn));
    }

    public WorkoutViewModel(UserRepository userRepo, WorkoutRepository workoutRepo) {
        this.user        = userRepo.findFirst()
                                   .orElseThrow(() -> new IllegalStateException("No user found"));
        this.workoutRepo = workoutRepo;
        refreshRecent();
    }

    public void logWorkout(WorkoutType type, int durationMinutes, Double distanceKm) {
        workoutRepo.logWorkout(user.getId(), type, durationMinutes, distanceKm, LocalDate.now());
        refreshRecent();
    }

    private void refreshRecent() {
        recentWorkouts.setAll(workoutRepo.getRecentWorkouts(user.getId()));
    }

    // ── Weekly stats (computed from this week's list) ──

    public int getWeeklyWorkoutCount()   {
        return workoutRepo.getThisWeekWorkouts(user.getId()).size();
    }

    public int getWeeklyTotalMinutes() {
        return workoutRepo.getThisWeekWorkouts(user.getId()).stream()
                .mapToInt(WorkoutEntry::durationMinutes).sum();
    }

    public double getWeeklyTotalDistanceKm() {
        return workoutRepo.getThisWeekWorkouts(user.getId()).stream()
                .filter(e -> e.distanceKm() != null)
                .mapToDouble(WorkoutEntry::distanceKm).sum();
    }

    // ── Personal bests ──

    public double getMaxRunDistanceKm()       { return workoutRepo.getMaxRunDistanceKm(user.getId()); }
    public double getBestRunPaceMinPerKm()    { return workoutRepo.getBestRunPaceMinPerKm(user.getId()); }
    public Optional<WorkoutEntry> getLongestWorkout() { return workoutRepo.getLongestWorkout(user.getId()); }

    // ── Getters ──

    public ObservableList<WorkoutEntry> getRecentWorkouts() { return recentWorkouts; }
}
