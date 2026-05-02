package com.example.personaltrainerapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

/**
 * Builds the main TabPane used after onboarding.
 * Called by both Main (on launch) and OnboardingController (after setup).
 */
public class SceneManager {

    public static TabPane buildMainScene() throws IOException {
        Tab dashboardTab = loadTab("🏠  Dashboard", "DashboardView.fxml");
        Tab workoutTab   = loadTab("💪  Workouts",  "WorkoutView.fxml");
        Tab pushUpTab    = loadTab("🤸  Push-Ups",  "PushUpView.fxml");

        TabPane tabPane = new TabPane(dashboardTab, workoutTab, pushUpTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabPane;
    }

    private static Tab loadTab(String title, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                SceneManager.class.getResource(fxmlFile)
        );
        Tab tab = new Tab(title, loader.load());
        tab.setClosable(false);
        return tab;
    }
}
