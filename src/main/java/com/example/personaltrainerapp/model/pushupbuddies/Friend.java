package com.example.personaltrainerapp.model.pushupbuddies;

/**
 * Abstract class to define a Friend that competes with the User for pushups
 */
public abstract class Friend {
    /** Name of the Friend */
    private final String name;
    /** Total recorded pushups */
    private int totalPushUps;
    /** Probability of the friend doing push-ups on a given day */
    private final double pushUpProbability;
    /** Number of push-ups done on a given day */
    private final int dailyPushUps;

    /**
     * Constructor for a Friend
     * @param name - Name of the friend
     * @param pushUpProbability - Probability of a friend doing push-ups
     * @param dailyPushUps - Number of push-ups done in a single day
     */
    protected Friend(String name, double pushUpProbability, int dailyPushUps) {
        this.name              = name;
        this.pushUpProbability = pushUpProbability;
        this.dailyPushUps      = dailyPushUps;
        this.totalPushUps      = 0;
    }

    /**
     * Getter methods
     */
    public String getName()              { return name; }
    public int    getTotalPushUps()      { return totalPushUps; }
    public double getPushUpProbability() { return pushUpProbability; }
    public int    getDailyPushUps()      { return dailyPushUps; }

    /**
     * Setter methods
     */
    public void setTotalPushUps(int total) { this.totalPushUps = total; }

    /**
     * A method to describe the Friend's level
     * @return a String that represents the level of a friend
     */
    public abstract String getDescription();
}
