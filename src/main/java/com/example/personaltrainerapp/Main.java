package com.example.personaltrainerapp;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.database.DatabaseSchema;
import com.example.personaltrainerapp.services.FruitApiService;
import com.example.personaltrainerapp.services.FruityViceAPIService;
import com.example.personaltrainerapp.ui.onboarding.OnboardingController;
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
        // Run the schema
        DatabaseSchema.init(connection);


        // Instantiate Fruity Vice API Service (can be mock-tested)
        FruitApiService apiService = new FruityViceAPIService();
        AppContext ctx = new AppContext(connection, apiService);

        // Route to onboarding on first launch, main tab view otherwise
        boolean isFirstLaunch = !ctx.userRepo.hasUser();

        Scene scene;
        if (isFirstLaunch) {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("OnboardingView.fxml"));
            loader.setControllerFactory(clazz -> new OnboardingController(ctx, () -> {
                try {
                    stage.setScene(new Scene(SceneManager.buildMainScene(ctx)));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load main view", e);
                }
            }));
            scene = new Scene(loader.load());
        } else {
            scene = new Scene(SceneManager.buildMainScene(ctx));
        }
        stage.setTitle("Personal Trainer App");
        stage.setScene(scene);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.show();
    }
}
