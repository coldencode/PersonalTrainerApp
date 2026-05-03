package com.example.personaltrainerapp.enums;

/**
 * Enumeration class to hold the workout types
 */
public enum WorkoutType {
    RUNNING ("Running", "🏃"),
    CYCLING ("Cycling", "🚴"),
    PUSH ("Push", "💪"),
    PULL ("Pull", "💪"),
    OTHER ("Other","🏋");

    private final String label;
    private final String icon;

    WorkoutType(String label, String icon) {
        this.label = label;
        this.icon  = icon;
    }

    public String getLabel() { return label; }
    public String getIcon()  { return icon; }

    /**
     * Method to retrieve an enum from SQLite database
     * @param label - The workout_type String stored in the SQLite Database
     * @return t - The workout_type as a WorkoutType enumeration or OTHER if doesnt exist
     */
    public static WorkoutType fromLabel(String label) {
        for (WorkoutType t : values()) {
            if (t.label.equals(label)) return t;
        }
        return OTHER;
    }

    /**
     * String representation of the enum
     */
    @Override
    public String toString() { return label; }
}
