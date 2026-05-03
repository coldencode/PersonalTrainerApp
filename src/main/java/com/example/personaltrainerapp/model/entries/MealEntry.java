package com.example.personaltrainerapp.model.entries;

import com.example.personaltrainerapp.enums.MealType;

import java.time.LocalDate;

/**
 * Holds the meal entry of a User
 * @param id - Unique identifier for a meal entry
 * @param userId - Id of the User
 * @param mealType - Meal type
 * @param calories - Calories of the meal
 * @param date - Date of the entry
 */
public record MealEntry(int id, int userId, MealType mealType, int calories, LocalDate date) {
}
