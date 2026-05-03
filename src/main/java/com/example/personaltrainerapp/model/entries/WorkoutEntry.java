package com.example.personaltrainerapp.model.entries;

import com.example.personaltrainerapp.enums.WorkoutType;

import java.time.LocalDate;

/**
 * Holds the Workout Entry of the User
 * @param id - Unique identifier
 * @param userId - UserID
 * @param type - Type of workout
 * @param durationMinutes - Time taken for this workout
 * @param distanceKm - Distance travelled for this workout
 * @param date - Date of log
 */
public record WorkoutEntry(int id, int userId, WorkoutType type, int durationMinutes, Double distanceKm, LocalDate date) {
}
