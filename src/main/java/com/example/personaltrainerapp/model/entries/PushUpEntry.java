package com.example.personaltrainerapp.model.entries;

import java.time.LocalDate;

/**
 * Record to hold the push-up entry of a User
 * @param id - Unique identifier
 * @param userId - User ID
 * @param count - Number of reps
 * @param date - Date of the entry
 */
public record PushUpEntry(int id, int userId, int count, LocalDate date) {
}
