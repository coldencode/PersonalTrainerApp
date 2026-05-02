package com.example.personaltrainerapp.model.entries;

import com.example.personaltrainerapp.enums.MealType;

import java.time.LocalDate;

public record MealEntry(int id, int userId, MealType mealType, int calories, LocalDate date) {
}
