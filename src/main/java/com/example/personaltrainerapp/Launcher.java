package com.example.personaltrainerapp;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.model.User;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        User user = new User();


        System.out.println("Database connected!");

        System.out.println(user);
        Application.launch(Main.class, args);
    }
}
