package com.example.personaltrainerapp.enums;

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

    public static WorkoutType fromLabel(String label) {
        for (WorkoutType t : values()) {
            if (t.label.equals(label)) return t;
        }
        return OTHER;
    }

    @Override
    public String toString() { return label; }
}
