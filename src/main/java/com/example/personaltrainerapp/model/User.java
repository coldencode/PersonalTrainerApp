package com.example.personaltrainerapp.model;

import java.time.LocalDate;

public class User {
    private int id;
    private String name;
    private double height;
    private double weight;
    private String goal;
    private LocalDate dateOfBirth;
    private String gender;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    private String weeklyGoal;
    public String getWeeklyGoal() { return weeklyGoal; }
    public void setWeeklyGoal(String weeklyGoal) { this.weeklyGoal = weeklyGoal; }
}
