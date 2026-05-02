package com.example.personaltrainerapp;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.repository.*;
import com.example.personaltrainerapp.services.FruityViceAPIService;
import com.example.personaltrainerapp.ui.dashboard.DashboardController;
import com.example.personaltrainerapp.ui.dashboard.DashboardViewModel;
import com.example.personaltrainerapp.ui.fruits.FruitsController;
import com.example.personaltrainerapp.ui.fruits.FruitsViewModel;
import com.example.personaltrainerapp.ui.pushup.PushUpController;
import com.example.personaltrainerapp.ui.pushup.PushUpViewModel;
import com.example.personaltrainerapp.ui.workout.WorkoutController;
import com.example.personaltrainerapp.ui.workout.WorkoutViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.sql.Connection;

/**
 * Builds the main TabPane used after onboarding.
 * Owns the single shared Connection and wires all ViewModels via setControllerFactory.
 * Called by both Main (on launch) and OnboardingController (after setup).
 */
public class SceneManager {

    public static TabPane buildMainScene() throws IOException {
        Connection conn = new DatabaseManager().getConnection();

        // ── Repositories (shared connection) ──
        UserRepository userRepo = new UserRepository(conn);
        WeightRepository weightRepo = new WeightRepository(conn);
        MealRepository mealRepo = new MealRepository(conn);
        WorkoutRepository workoutRepo = new WorkoutRepository(conn);
        PushUpRepository pushUpRepo = new PushUpRepository(conn);
        FriendRepository friendRepo = new FriendRepository(conn);

        // ── ViewModels ──
        DashboardViewModel dashboardVm = new DashboardViewModel(userRepo, weightRepo, mealRepo);
        WorkoutViewModel workoutVm = new WorkoutViewModel(userRepo, workoutRepo);
        PushUpViewModel pushUpVm = new PushUpViewModel(userRepo, pushUpRepo, friendRepo);
        FruitsViewModel fruitsVm = new FruitsViewModel(new FruityViceAPIService());

        // ── Tabs ──
        Tab dashboardTab = loadTab("Dashboard", "DashboardView.fxml", clazz -> new DashboardController(dashboardVm));
        Tab workoutTab   = loadTab("Workouts",  "WorkoutView.fxml",   clazz -> new WorkoutController(workoutVm));
        Tab pushUpTab    = loadTab("Push-Ups",  "PushUpView.fxml",    clazz -> new PushUpController(pushUpVm));
        Tab fruitsTab    = loadTab("Fruits",    "FruitsView.fxml",    clazz -> new FruitsController(fruitsVm));

        TabPane tabPane = new TabPane(dashboardTab, workoutTab, pushUpTab, fruitsTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabPane;
    }

    private static Tab loadTab(String title, String fxmlFile,
                               javafx.util.Callback<Class<?>, Object> factory) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
        loader.setControllerFactory(factory);
        Tab tab = new Tab(title, loader.load());
        tab.setClosable(false);
        return tab;
    }
}
