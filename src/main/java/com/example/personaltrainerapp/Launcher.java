package com.example.personaltrainerapp;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.model.User;
import javafx.application.Application;

import java.sql.Connection;

public class Launcher {
    public static void main(String[] args) {
        User user = new User();
        Connection conn = DatabaseManager.connect();

        System.out.println("Database connected!");

        System.out.println(user);
        Application.launch(HelloApplication.class, args);
    }
}
