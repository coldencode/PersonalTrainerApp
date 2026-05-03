package com.example.personaltrainerapp.model.pushupbuddies;

/**
 * Class to represent Brandon who is a super-buff Friend that often does push-ups
 */
public class Brandon extends Friend {

    public Brandon() {
        super("Brandon", 0.85, 80);
    }

    @Override
    public String getDescription() { return "Super Buff"; }
}
