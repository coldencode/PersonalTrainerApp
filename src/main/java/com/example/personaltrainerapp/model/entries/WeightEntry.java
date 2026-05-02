package com.example.personaltrainerapp.model.entries;

import java.time.LocalDate;

public record WeightEntry(int id, double weight, LocalDate date) {
}
