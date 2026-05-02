package com.example.personaltrainerapp.model.entries;

import java.time.LocalDate;

public record DailyCalorieEntry(LocalDate date, int totalCalories) {
}
