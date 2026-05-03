package com.example.personaltrainerapp.ui.onboarding;

import com.example.personaltrainerapp.AppContext;
import com.example.personaltrainerapp.enums.UserGoal;
import com.example.personaltrainerapp.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Controller to handle the large onboarding screen along with the UI logic
 */
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

    private final AppContext ctx;
    private final Runnable onComplete;
    private int currentStep = 1;

    public OnboardingController(AppContext ctx, Runnable onComplete) {
        this.ctx        = ctx;
        this.onComplete = onComplete;
    }

    @FXML
    public void initialize() {
        genderCombo.getItems().addAll("Male", "Female", "Other", "Prefer not to say");
        showStep(1);
    }

    /**
     * Handles all the steps of the screen
     * @param step
     */
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
        UserGoal goal = selectedGoal != null ? UserGoal.fromLabel(selectedGoal.getText()) : null;

        weeklyGoalGroup.selectToggle(null);

        if (goal == null) return;
        switch (goal) {
            case GAIN_WEIGHT -> {
                weeklyGoalTitle.setText("How fast do you want to gain?");
                weeklyOption1.setText("Gain 0.2 kg / week  (slow & steady)");
                weeklyOption2.setText("Gain 0.5 kg / week  (moderate)");
                weeklyOption3.setText("Gain 1.0 kg / week  (aggressive)");
            }
            case LOSE_WEIGHT -> {
                weeklyGoalTitle.setText("How fast do you want to lose?");
                weeklyOption1.setText("Lose 0.2 kg / week  (slow & steady)");
                weeklyOption2.setText("Lose 0.5 kg / week  (moderate)");
                weeklyOption3.setText("Lose 1.0 kg / week  (aggressive)");
            }
            default -> {
                weeklyGoalTitle.setText("What's your weekly focus?");
                weeklyOption1.setText("Keep my current weight");
                weeklyOption2.setText("Improve body composition");
                weeklyOption3.setText("Build endurance & fitness");
            }
        }
    }

    /**
     * Handles the next button
     */
    @FXML
    private void onNext() {
        if (currentStep < 4) {
            showStep(currentStep + 1);
        } else {
            saveAndProceed();
        }
    }

    /**
     * Handles the back button
     */
    @FXML
    private void onBack() {
        if (currentStep > 1) showStep(currentStep - 1);
    }

    /**
     * Handles the Save button and proceeds to run the SceneManager to build the Tabs
     */
    private void saveAndProceed() {
        User user = new User();
        user.setName(nameField.getText().trim());

        RadioButton selectedGoal = (RadioButton) goalGroup.getSelectedToggle();
        if (selectedGoal != null) user.setGoal(UserGoal.fromLabel(selectedGoal.getText()));

        try {
            user.setHeight(Double.parseDouble(heightField.getText().trim()));
            user.setWeight(Double.parseDouble(weightField.getText().trim()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid number!");
        }

        user.setDateOfBirth(dobPicker.getValue());
        user.setGender(genderCombo.getValue());

        RadioButton selectedWeekly = (RadioButton) weeklyGoalGroup.getSelectedToggle();
        if (selectedWeekly != null) user.setWeeklyGoal(selectedWeekly.getText());

        ctx.userRepo.save(user);
        // On complete, will run the SceneManager
        onComplete.run();
    }
}
