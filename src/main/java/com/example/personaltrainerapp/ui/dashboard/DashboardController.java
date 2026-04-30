package com.example.personaltrainerapp.ui.dashboard;

import com.example.personaltrainerapp.model.WeightEntry;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label calorieGoalLabel;
    @FXML private Label weeklyGoalLabel;
    @FXML private LineChart<String, Number> weightChart;

    private final DashboardViewModel vm = new DashboardViewModel();

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, " + vm.getUser().getName() + "!");
        calorieGoalLabel.setText(String.valueOf(vm.getDailyCalories()));

        String weekly = vm.getUser().getWeeklyGoal();
        weeklyGoalLabel.setText(weekly != null ? weekly : "—");

        refreshChart();
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
