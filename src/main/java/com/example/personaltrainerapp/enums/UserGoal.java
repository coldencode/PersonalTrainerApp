package com.example.personaltrainerapp.enums;


/**
 * Enumeration class to hold the goals of a User
 */
public enum UserGoal {
    /** If User wants to lose weight goal **/
    LOSE_WEIGHT ("Lose Weight"),
    /** If User wants to maintain weight goal **/
    MAINTAIN_WEIGHT("Maintain Weight"),
    /** If User wants to gain weight goal **/
    GAIN_WEIGHT("Gain Weight");

    private final String label;

    UserGoal(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    /**
     * Method to retrieve an enum from SQLite database
     * @param label - The user_goal stored in the SQLite Database
     * @return g - The user_goal as an UserGoal enumeration
     */
    public static UserGoal fromLabel(String label) {
        if (label == null) return null;
        for (UserGoal g : values()) {
            if (g.label.equals(label)) return g;
        }
        return null;
    }

    /**
     * String representation of the enum
     */
    @Override
    public String toString() { return label; }
}
