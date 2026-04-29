package com.example.personaltrainerapp;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.database.DatabaseSchema;
import com.example.personaltrainerapp.repository.UserRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class Main extends Application {

    private final DatabaseManager db = new DatabaseManager();

    @Override
    public void start(Stage stage) throws IOException {

        // Start-up database
        Connection connection = db.getConnection();
        DatabaseSchema.init(connection);

        // Route to onboarding on first launch, dashboard otherwise
        boolean isFirstLaunch = !new UserRepository(connection).hasUser();
        String view = isFirstLaunch ? "OnboardingView.fxml" : "DashboardView.fxml";

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(view));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Personal Trainer App");
        stage.setScene(scene);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.show();
    }
}
