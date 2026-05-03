package com.example.personaltrainerapp.enums;

/**
 * Enumeration class to hold the meal types of a User
 * which are Breakfast, Lunch, Dinner
 */
public enum MealType {
    BREAKFAST("Breakfast"),
    LUNCH    ("Lunch"),
    DINNER   ("Dinner");

    private final String label;

    MealType(String label) {
        this.label = label;
    }

    /**
     * Getter method to retrieve the label of a Meal Type
     */
    public String getLabel() { return label; }

    /**
     * Method to retrieve an enum from SQLite database
     * @param label - The meal_type stored in the SQLite Database
     * @return t - The meal_type as a MealType enumeration
     */
    public static MealType fromLabel(String label) {
        for (MealType t : values()) {
            if (t.label.equals(label)) return t;
        }
        throw new IllegalArgumentException("Unknown meal type: " + label);
    }

    /**
     * String representation of the enum
     */
    @Override
    public String toString() { return label; }
}
