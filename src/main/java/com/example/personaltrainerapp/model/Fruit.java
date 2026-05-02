package com.example.personaltrainerapp.model;

public record Fruit(
        String name,
        String family,
        String genus,
        double calories,
        double fat,
        double sugar,
        double carbohydrates,
        double protein
) {
}