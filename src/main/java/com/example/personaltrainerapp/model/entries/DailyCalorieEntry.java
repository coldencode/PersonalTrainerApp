package com.example.personaltrainerapp.model.entries;

import java.time.LocalDate;

/**
 * Record to hold the daily calories entries of the User
 * This record is only used for graphing and derived from the MealEntry
 * @param date - Date of entry
 * @param totalCalories - Total calories inputted on this date
 */
public record DailyCalorieEntry(LocalDate date, int totalCalories) {
}
