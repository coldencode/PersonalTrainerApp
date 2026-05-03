package com.example.personaltrainerapp.model.pushupbuddies;

/**
 * Class to represent Simon, a friend that occassionally does push-ups
 */
public class Simon extends Friend {

    public Simon() {
        super("Simon", 0.60, 40);
    }

    @Override
    public String getDescription() { return "Buff"; }
}
