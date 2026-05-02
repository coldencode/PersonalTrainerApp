package com.example.personaltrainerapp.ui.pushup;

import com.example.personaltrainerapp.model.pushupbuddies.Friend;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PushUpController {

    // Left panel
    @FXML private ListView<String> recentListView;

    // Right top — stats
    @FXML private Label weeklyTotalLabel;
    @FXML private Label monthlyTotalLabel;
    @FXML private Label overallTotalLabel;

    // Right top — competition
    @FXML private Button selectFriendButton;
    @FXML private VBox   friendStatsBox;
    @FXML private Label  friendNameLabel;
    @FXML private Label  friendTotalLabel;
    @FXML private Label  friendDescLabel;
    @FXML private Label  versusLabel;

    // Right bottom — chart
    @FXML private LineChart<String, Number> pushUpChart;

    private final PushUpViewModel vm;

    public PushUpController() {
        this(new PushUpViewModel());
    }

    public PushUpController(PushUpViewModel vm) {
        this.vm = vm;
    }

    @FXML
    public void initialize() {
        refresh();
        refreshFriend();
    }

    @FXML
    private void onLogPushUps() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Log Push-Ups");
        dialog.setHeaderText("How many push-ups did you do?");
        dialog.setContentText("Count:");
        dialog.showAndWait().ifPresent(input -> {
            try {
                int count = Integer.parseInt(input.trim());
                if (count > 0) {
                    vm.logPushUps(count);
                    refresh();
                    refreshFriend(); // update vs. display after new log
                }
            } catch (NumberFormatException ignored) {
                // TODO: show validation alert
            }
        });
    }

    @FXML
    private void onSelectFriend() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Louis",
                List.of("Louis (Chill)", "Simon (Buff)", "Brandon (Super Buff)"));
        dialog.setTitle("Compete with a Friend");
        dialog.setHeaderText("Choose your rival — you cannot change this later!");
        dialog.setContentText("Friend:");

        dialog.showAndWait().ifPresent(choice -> {
            // Extract just the first name
            String name = choice.split(" ")[0];
            vm.selectFriend(name);
            refreshFriend();
        });
    }

    private void refresh() {
        refreshList();
        refreshStats();
        refreshChart();
    }

    private void refreshList() {
        recentListView.getItems().clear();
        for (var entry : vm.getRecentEntries()) {
            recentListView.getItems().add(
                    String.format("💪  %d reps  —  %s", entry.count(), entry.date())
            );
        }
    }

    private void refreshStats() {
        weeklyTotalLabel.setText(String.valueOf(vm.getWeeklyTotal()));
        monthlyTotalLabel.setText(String.valueOf(vm.getMonthlyTotal()));
        overallTotalLabel.setText(String.valueOf(vm.getOverallTotal()));
    }

    private void refreshChart() {
        pushUpChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Push-Ups");

        Map<LocalDate, Integer> daily = vm.getDailyChartData();
        for (Map.Entry<LocalDate, Integer> entry : daily.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        pushUpChart.getData().add(series);
    }

    private void refreshFriend() {
        if (vm.hasFriend()) {
            selectFriendButton.setVisible(false);
            selectFriendButton.setManaged(false);

            Optional<Friend> friendOpt = vm.getFriend();
            if (friendOpt.isPresent()) {
                Friend f = friendOpt.get();
                friendNameLabel.setText(f.getName() + "  (" + f.getDescription() + ")");
                friendTotalLabel.setText(String.valueOf(f.getTotalPushUps()));

                int myTotal = vm.getOverallTotal();
                int diff    = myTotal - f.getTotalPushUps();
                if (diff > 0) {
                    versusLabel.setText("You're ahead by " + diff + " 🏆");
                    versusLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");
                } else if (diff < 0) {
                    versusLabel.setText(f.getName() + " is ahead by " + Math.abs(diff) + " 😤");
                    versusLabel.setStyle("-fx-text-fill: #C62828; -fx-font-weight: bold;");
                } else {
                    versusLabel.setText("It's a tie! 🤝");
                    versusLabel.setStyle("-fx-text-fill: #F57C00; -fx-font-weight: bold;");
                }

                friendStatsBox.setVisible(true);
                friendStatsBox.setManaged(true);
            }
        } else {
            selectFriendButton.setVisible(true);
            selectFriendButton.setManaged(true);
            friendStatsBox.setVisible(false);
            friendStatsBox.setManaged(false);
        }
    }
}
