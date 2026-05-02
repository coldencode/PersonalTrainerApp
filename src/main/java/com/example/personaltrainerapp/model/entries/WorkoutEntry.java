package com.example.personaltrainerapp.model.entries;

import com.example.personaltrainerapp.enums.WorkoutType;

import java.time.LocalDate;

public record WorkoutEntry(int id, int userId, WorkoutType type, int durationMinutes, Double distanceKm, LocalDate date) {
}
