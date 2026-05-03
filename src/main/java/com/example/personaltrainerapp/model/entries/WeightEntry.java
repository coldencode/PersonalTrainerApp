package com.example.personaltrainerapp.model.entries;

import java.time.LocalDate;

/**
 * Record to hold the weight of the user
 * @param id - Unique identifier
 * @param weight - Current weight of the User on this date
 * @param date - Date of entry
 */
public record WeightEntry(int id, double weight, LocalDate date) {
}
