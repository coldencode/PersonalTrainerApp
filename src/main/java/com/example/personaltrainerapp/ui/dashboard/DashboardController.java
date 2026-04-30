package com.example.personaltrainerapp.ui.dashboard;

import com.example.personaltrainerapp.enums.MealType;
import com.example.personaltrainerapp.enums.UserGoal;
import com.example.personaltrainerapp.model.WeightEntry;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.util.List;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label calorieGoalLabel;
    @FXML private Label weeklyGoalLabel;
    @FXML private Label intakeLabel;
    @FXML private Label remainingLabel;
    @FXML private LineChart<String, Number> calorieHistoryChart;
    @FXML private Label bmiValueLabel;
    @FXML private Label bmiCategoryLabel;
    @FXML private LineChart<String, Number> weightChart;

    private final DashboardViewModel vm = new DashboardViewModel();

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, " + vm.getUser().getName() + "!");
        calorieGoalLabel.setText(String.valueOf(vm.getDailyCalories()));

        String weekly = vm.getUser().getWeeklyGoal();
        weeklyGoalLabel.setText(weekly != null ? weekly : "—");

        double bmi = vm.getBmi();
        bmiValueLabel.setText(bmi >= 0 ? String.valueOf(bmi) : "—");
        bmiCategoryLabel.setText(vm.getBmiCategory());

        // Bind intake & remaining reactively so they update on every meal log
        intakeLabel.textProperty().bind(vm.todayIntakeProperty().asString());
        vm.todayIntakeProperty().addListener((obs, oldVal, newVal) -> {
            updateRemaining(newVal.intValue());
            refreshCalorieChart();
        });
        updateRemaining(vm.todayIntakeProperty().get());

        refreshCalorieChart();
        refreshChart();
    }

    @FXML
    private void onChangeWeeklyGoal() {
        UserGoal goal = vm.getUser().getGoal();
        List<String> options = (goal == UserGoal.GAIN_WEIGHT) ? List.of(
                "Gain 0.2 kg / week  (slow & steady)",
                "Gain 0.5 kg / week  (moderate)",
                "Gain 1.0 kg / week  (aggressive)"
        ) : (goal == UserGoal.LOSE_WEIGHT) ? List.of(
                "Lose 0.2 kg / week  (slow & steady)",
                "Lose 0.5 kg / week  (moderate)",
                "Lose 1.0 kg / week  (aggressive)"
        ) : List.of(
                "Keep my current weight",
                "Improve body composition",
                "Build endurance & fitness"
        );

        String current = vm.getUser().getWeeklyGoal();
        String defaultChoice = (current != null && options.contains(current)) ? current : options.get(0);

        ChoiceDialog<String> dialog = new ChoiceDialog<>(defaultChoice, options);
        dialog.setTitle("Change Weekly Goal");
        dialog.setHeaderText("Select a new weekly goal");
        dialog.setContentText("Goal:");
        dialog.showAndWait().ifPresent(selected -> {
            vm.updateWeeklyGoal(selected);
            weeklyGoalLabel.setText(selected);
            calorieGoalLabel.setText(String.valueOf(vm.getDailyCalories()));
            updateRemaining(vm.todayIntakeProperty().get());
        });
    }

    @FXML private void onLogBreakfast() { showLogDialog(MealType.BREAKFAST); }
    @FXML private void onLogLunch()     { showLogDialog(MealType.LUNCH); }
    @FXML private void onLogDinner()    { showLogDialog(MealType.DINNER); }

    private void showLogDialog(MealType mealType) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Log " + mealType.getLabel());
        dialog.setHeaderText("How many calories in your " + mealType.getLabel().toLowerCase() + "?");
        dialog.setContentText("Calories:");
        dialog.showAndWait().ifPresent(input -> {
            try {
                int calories = Integer.parseInt(input.trim());
                vm.logMeal(mealType, calories);
            } catch (NumberFormatException e) {
                // TODO: show inline validation message
            }
        });
    }

    private void updateRemaining(int intake) {
        int remaining = vm.getDailyCalories() - intake;
        if (remaining >= 0) {
            remainingLabel.setText(remaining + " kcal remaining");
            remainingLabel.setStyle("-fx-text-fill: #27AE60; -fx-font-size: 13px;");
        } else {
            remainingLabel.setText(Math.abs(remaining) + " kcal over goal");
            remainingLabel.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 13px;");
        }
    }

    @FXML
    private void onAddWeight() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Log Weight");
        dialog.setHeaderText("Add today's weight");
        dialog.setContentText("Weight (kg):");
        dialog.showAndWait().ifPresent(input -> {
            try {
                double weight = Double.parseDouble(input.trim());
                vm.addWeight(weight);
                refreshChart();
            } catch (NumberFormatException e) {
                // TODO: show inline validation message
            }
        });
    }

    private void refreshCalorieChart() {
        calorieHistoryChart.getData().clear();

        // Series 1 — actual daily intake
        XYChart.Series<String, Number> intakeSeries = new XYChart.Series<>();
        intakeSeries.setName("Intake");
        for (var entry : vm.getCalorieDailyTotals()) {
            intakeSeries.getData().add(new XYChart.Data<>(entry.date().toString(), entry.totalCalories()));
        }

        // Series 2 — flat goal line across the same dates
        XYChart.Series<String, Number> goalSeries = new XYChart.Series<>();
        goalSeries.setName("Goal");
        for (var entry : vm.getCalorieDailyTotals()) {
            goalSeries.getData().add(new XYChart.Data<>(entry.date().toString(), vm.getDailyCalories()));
        }

        calorieHistoryChart.getData().addAll(intakeSeries, goalSeries);
    }

    private void refreshChart() {
        weightChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (WeightEntry entry : vm.getWeightEntries()) {
            series.getData().add(
                    new XYChart.Data<>(entry.date().toString(), entry.weight())
            );
        }
        weightChart.getData().add(series);
    }
}
