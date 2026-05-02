package com.example.personaltrainerapp.ui.workout;

import com.example.personaltrainerapp.enums.WorkoutType;
import com.example.personaltrainerapp.model.entries.WorkoutEntry;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class WorkoutController {

    @FXML private ListView<String> recentListView;

    // Weekly stats
    @FXML private Label weekCountLabel;
    @FXML private Label weekMinutesLabel;
    @FXML private Label weekDistanceLabel;

    // Personal bests
    @FXML private Label bestRunLabel;
    @FXML private Label bestPaceLabel;
    @FXML private Label longestSessionLabel;

    private final WorkoutViewModel vm = new WorkoutViewModel();

    @FXML
    public void initialize() {
        refresh();
    }

    @FXML private void onLogWorkout() { showWorkoutDialog(false); }
    @FXML private void onLogRun()     { showWorkoutDialog(true); }

    private void showWorkoutDialog(boolean preSelectRun) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(preSelectRun ? "Log a Run" : "Log a Workout");
        dialog.setHeaderText(preSelectRun ? "Track your run" : "Log a workout session");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<WorkoutType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(WorkoutType.values());
        typeCombo.setValue(preSelectRun ? WorkoutType.RUNNING : WorkoutType.PUSH);

        TextField durationField = new TextField();
        durationField.setPromptText("e.g. 30");

        Label distanceLabel = new Label("Distance (km):");
        TextField distanceField = new TextField();
        distanceField.setPromptText("e.g. 5.0");

        // Show distance row only for Running
        Runnable updateVisibility = () -> {
            boolean running = typeCombo.getValue() == WorkoutType.RUNNING;
            distanceLabel.setVisible(running); distanceLabel.setManaged(running);
            distanceField.setVisible(running); distanceField.setManaged(running);
        };
        typeCombo.setOnAction(e -> updateVisibility.run());
        updateVisibility.run();

        VBox content = new VBox(8,
                new Label("Type:"),    typeCombo,
                new Label("Duration (minutes):"), durationField,
                distanceLabel, distanceField
        );
        content.setStyle("-fx-padding: 10;");
        dialog.getDialogPane().setContent(content);

        dialog.showAndWait().ifPresent(btn -> {
            if (btn != ButtonType.OK) return;
            try {
                WorkoutType type     = typeCombo.getValue();
                int         duration = Integer.parseInt(durationField.getText().trim());
                Double      distance = null;
                if (type == WorkoutType.RUNNING && !distanceField.getText().trim().isEmpty()) {
                    distance = Double.parseDouble(distanceField.getText().trim());
                }
                vm.logWorkout(type, duration, distance);
                refresh();
            } catch (NumberFormatException ignored) {
                // TODO: show validation alert
            }
        });
    }

    private void refresh() {
        refreshRecentList();
        refreshWeeklyStats();
        refreshPersonalBests();
    }

    private void refreshRecentList() {
        recentListView.getItems().clear();
        for (WorkoutEntry e : vm.getRecentWorkouts()) {
            String dist = (e.distanceKm() != null)
                    ? String.format("  •  %.1f km", e.distanceKm()) : "";
            recentListView.getItems().add(
                    String.format("%s  %s  •  %d min%s  —  %s",
                            e.type().getIcon(), e.type(), e.durationMinutes(), dist, e.date())
            );
        }
    }

    private void refreshWeeklyStats() {
        weekCountLabel.setText(String.valueOf(vm.getWeeklyWorkoutCount()));
        weekMinutesLabel.setText(String.valueOf(vm.getWeeklyTotalMinutes()));
        weekDistanceLabel.setText(String.format("%.1f", vm.getWeeklyTotalDistanceKm()));
    }

    private void refreshPersonalBests() {
        double maxDist = vm.getMaxRunDistanceKm();
        bestRunLabel.setText(maxDist > 0
                ? String.format("🏃  Longest Run:       %.1f km", maxDist)
                : "🏃  Longest Run:       —");

        double pace = vm.getBestRunPaceMinPerKm();
        if (pace > 0) {
            int paceMin = (int) pace;
            int paceSec = (int) Math.round((pace - paceMin) * 60);
            bestPaceLabel.setText(String.format("⚡  Best Pace:         %d:%02d min/km", paceMin, paceSec));
        } else {
            bestPaceLabel.setText("⚡  Best Pace:         —");
        }

        Optional<WorkoutEntry> longest = vm.getLongestWorkout();
        longestSessionLabel.setText(longest
                .map(e -> String.format("💪  Longest Session:  %d min  (%s)", e.durationMinutes(), e.type()))
                .orElse("💪  Longest Session:  —"));
    }
}
