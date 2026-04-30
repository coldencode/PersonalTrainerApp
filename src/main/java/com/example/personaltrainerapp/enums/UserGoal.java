package com.example.personaltrainerapp.enums;

public enum UserGoal {
    LOSE_WEIGHT    ("Lose Weight"),
    MAINTAIN_WEIGHT("Maintain Weight"),
    GAIN_WEIGHT    ("Gain Weight");

    private final String label;

    UserGoal(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    public static UserGoal fromLabel(String label) {
        if (label == null) return null;
        for (UserGoal g : values()) {
            if (g.label.equals(label)) return g;
        }
        return null;
    }

    @Override
    public String toString() { return label; }
}
