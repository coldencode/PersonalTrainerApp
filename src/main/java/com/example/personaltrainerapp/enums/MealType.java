package com.example.personaltrainerapp.enums;

public enum MealType {
    BREAKFAST("Breakfast"),
    LUNCH    ("Lunch"),
    DINNER   ("Dinner");

    private final String label;

    MealType(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    public static MealType fromLabel(String label) {
        for (MealType t : values()) {
            if (t.label.equals(label)) return t;
        }
        throw new IllegalArgumentException("Unknown meal type: " + label);
    }

    @Override
    public String toString() { return label; }
}
