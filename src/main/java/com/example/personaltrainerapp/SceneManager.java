package com.example.personaltrainerapp;

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

/**
 * Builds the main TabPane from a fully-wired AppContext.
 * Responsible only for UI assembly — no connection or repository creation here.
 */
public class SceneManager {

    public static TabPane buildMainScene(AppContext ctx) throws IOException {
        DashboardViewModel dashboardVm = new DashboardViewModel(ctx.userRepo, ctx.weightRepo, ctx.mealRepo);
        WorkoutViewModel   workoutVm   = new WorkoutViewModel(ctx.userRepo, ctx.workoutRepo);
        PushUpViewModel    pushUpVm    = new PushUpViewModel(ctx.userRepo, ctx.pushUpRepo, ctx.friendRepo);
        FruitsViewModel    fruitsVm    = new FruitsViewModel(ctx.fruitApiService);

        Tab dashboardTab = loadTab("Dashboard", "DashboardView.fxml", c -> new DashboardController(dashboardVm));
        Tab workoutTab   = loadTab("Workouts",  "WorkoutView.fxml",   c -> new WorkoutController(workoutVm));
        Tab pushUpTab    = loadTab("Push-Ups",  "PushUpView.fxml",    c -> new PushUpController(pushUpVm));
        Tab fruitsTab    = loadTab("Fruits",    "FruitsView.fxml",    c -> new FruitsController(fruitsVm));

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
