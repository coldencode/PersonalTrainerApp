package com.example.personaltrainerapp;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.database.DatabaseSchema;
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

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


    }
}
