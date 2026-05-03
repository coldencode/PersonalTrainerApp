package com.example.personaltrainerapp.model.pushupbuddies;

/**
 * Class to represent Louis who is a casual Friend that rarely does push-ups
 */
public class Louis extends Friend {

    public Louis() {
        super("Louis", 0.30, 15);
    }

    @Override
    public String getDescription() { return "Casual"; }
}
