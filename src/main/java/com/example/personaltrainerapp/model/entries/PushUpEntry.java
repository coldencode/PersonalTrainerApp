package com.example.personaltrainerapp.model.entries;

import java.time.LocalDate;

public record PushUpEntry(int id, int userId, int count, LocalDate date) {
}
