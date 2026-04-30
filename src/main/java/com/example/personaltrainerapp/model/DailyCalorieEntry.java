package com.example.personaltrainerapp.model;

import java.time.LocalDate;

public record DailyCalorieEntry(LocalDate date, int totalCalories) {
}
