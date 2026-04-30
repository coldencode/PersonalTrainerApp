package com.example.personaltrainerapp.model;

import java.time.LocalDate;

public record WeightEntry(int id, double weight, LocalDate date) {
}
