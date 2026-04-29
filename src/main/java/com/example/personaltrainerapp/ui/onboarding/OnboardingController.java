package com.example.personaltrainerapp.ui.onboarding;

import com.example.personaltrainerapp.database.DatabaseManager;
import com.example.personaltrainerapp.repository.UserRepository;
import com.example.personaltrainerapp.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class OnboardingController {

    // Step panels
    @FXML private VBox step1, step2, step3, step4;

    // Step 1 - Name
    @FXML private TextField nameField;

    // Step 2 - Goal
    @FXML private ToggleGroup goalGroup;

    // Step 3 - About you
    @FXML private TextField heightField;
    @FXML private TextField weightField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderCombo;

    // Step 4 - Weekly goal (populated dynamically)
    @FXML private Label weeklyGoalTitle;
    @FXML private ToggleGroup weeklyGoalGroup;
    @FXML private RadioButton weeklyOption1, weeklyOption2, weeklyOption3;

    private int currentStep = 1;

    @FXML
    public void initialize() {
        genderCombo.getItems().addAll("Male", "Female", "Other", "Prefer not to say");
        showStep(1);
    }

    private void showStep(int step) {
        step1.setVisible(step == 1); step1.setManaged(step == 1);
        step2.setVisible(step == 2); step2.setManaged(step == 2);
        step3.setVisible(step == 3); step3.setManaged(step == 3);
        step4.setVisible(step == 4); step4.setManaged(step == 4);

        if (step == 4) populateWeeklyGoalOptions();

        currentStep = step;
    }

    /** Fills step 4's title and radio buttons based on the goal chosen in step 2. */
    private void populateWeeklyGoalOptions() {
        RadioButton selectedGoal = (RadioButton) goalGroup.getSelectedToggle();
        String goal = selectedGoal != null ? selectedGoal.getText() : "";

        weeklyGoalGroup.selectToggle(null);

        switch (goal) {
            case "Gain Weight" -> {
                weeklyGoalTitle.setText("How fast do you want to gain?");
                weeklyOption1.setText("Gain 0.2 kg / week  (slow & steady)");
                weeklyOption2.setText("Gain 0.5 kg / week  (moderate)");
                weeklyOption3.setText("Gain 1.0 kg / week  (aggressive)");
            }
            case "Lose Weight" -> {
                weeklyGoalTitle.setText("How fast do you want to lose?");
                weeklyOption1.setText("Lose 0.2 kg / week  (slow & steady)");
                weeklyOption2.setText("Lose 0.5 kg / week  (moderate)");
                weeklyOption3.setText("Lose 1.0 kg / week  (aggressive)");
            }
            default -> {
                // Maintain Weight — only one option needed
                weeklyGoalTitle.setText("What's your weekly focus?");
                weeklyOption1.setText("Keep my current weight");
                weeklyOption2.setText("Improve body composition");
                weeklyOption3.setText("Build endurance & fitness");
            }
        }
    }

    @FXML
    private void onNext() {
        if (currentStep < 4) {
            showStep(currentStep + 1);
        } else {
            saveAndProceed();
        }
    }

    @FXML
    private void onBack() {
        if (currentStep > 1) showStep(currentStep - 1);
    }

    private void saveAndProceed() {
        User user = new User();
        user.setName(nameField.getText().trim());

        RadioButton selectedGoal = (RadioButton) goalGroup.getSelectedToggle();
        if (selectedGoal != null) user.setGoal(selectedGoal.getText());

        try {
            user.setHeight(Double.parseDouble(heightField.getText().trim()));
            user.setWeight(Double.parseDouble(weightField.getText().trim()));
        } catch (NumberFormatException e) {
            // TODO: show validation error
        }

        user.setDateOfBirth(dobPicker.getValue());
        user.setGender(genderCombo.getValue());

        RadioButton selectedWeekly = (RadioButton) weeklyGoalGroup.getSelectedToggle();
        if (selectedWeekly != null) user.setWeeklyGoal(selectedWeekly.getText());

        DatabaseManager db = new DatabaseManager();
        new UserRepository(db.getConnection()).save(user);

        // Switch to Dashboard
        try {
            FXMLLoader loader = new FXMLLoader(
                    OnboardingController.class.getResource("/com/example/personaltrainerapp/DashboardView.fxml")
            );
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Dashboard", e);
        }
    }
}
