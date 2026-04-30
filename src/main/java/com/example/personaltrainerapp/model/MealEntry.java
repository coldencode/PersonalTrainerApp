package com.example.personaltrainerapp.model;

import java.time.LocalDate;

public record MealEntry(int id, int userId, String mealType, int calories, LocalDate date) {
}
