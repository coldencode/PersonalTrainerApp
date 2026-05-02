package com.example.personaltrainerapp.model.pushupbuddies;

public abstract class Friend {

    private final String name;
    private int totalPushUps;
    private final double pushUpProbability;
    private final int dailyPushUps;

    protected Friend(String name, double pushUpProbability, int dailyPushUps) {
        this.name              = name;
        this.pushUpProbability = pushUpProbability;
        this.dailyPushUps      = dailyPushUps;
        this.totalPushUps      = 0;
    }

    public String getName()              { return name; }
    public int    getTotalPushUps()      { return totalPushUps; }
    public double getPushUpProbability() { return pushUpProbability; }
    public int    getDailyPushUps()      { return dailyPushUps; }

    public void setTotalPushUps(int total) { this.totalPushUps = total; }

    public abstract String getDescription();
}
