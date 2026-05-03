package com.example.personaltrainerapp.ui.fruits;

import com.example.personaltrainerapp.model.Fruit;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FruitsController {

    @FXML private TextField searchField;
    @FXML private TableView<Fruit> fruitsTable;
    @FXML private TableColumn<Fruit, String> nameCol;
    @FXML private TableColumn<Fruit, String> familyCol;
    @FXML private TableColumn<Fruit, String> genusCol;
    @FXML private TableColumn<Fruit, Double> caloriesCol;
    @FXML private TableColumn<Fruit, Double> sugarCol;
    @FXML private TableColumn<Fruit, Double> carbsCol;
    @FXML private TableColumn<Fruit, Double> proteinCol;
    @FXML private TableColumn<Fruit, Double> fatCol;
    @FXML private Label statusLabel;

    private final FruitsViewModel vm;

    public FruitsController(FruitsViewModel vm) {
        this.vm = vm;
    }

    @FXML
    public void initialize() {
        // Set the table values with functions
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().name()));
        familyCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().family()));
        genusCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().genus()));
        caloriesCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().calories()));
        sugarCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().sugar()));
        carbsCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().carbohydrates()));
        proteinCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().protein()));
        fatCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().fat()));

        // Create a list of filters
        FilteredList<Fruit> filtered = new FilteredList<>(vm.getFruits(), f -> true);
        fruitsTable.setItems(filtered);

        // Add listener when search field changes
        searchField.textProperty().addListener((obs, old, val) -> {
            String lower = val.toLowerCase();
            filtered.setPredicate(f ->
                    val.isBlank() ||
                    f.name().toLowerCase().contains(lower) ||
                    f.family().toLowerCase().contains(lower) ||
                    f.genus().toLowerCase().contains(lower)
            );
        });

        statusLabel.setText("Loading fruits...");

        // Runs on a separate thread
        Thread thread = new Thread(() -> {
            try {
                vm.loadFruits();
                Platform.runLater(() ->
                        statusLabel.setText(vm.getFruits().size() + " fruits loaded from Fruityvice API")
                );
            } catch (Exception e) {
                Platform.runLater(() ->
                        statusLabel.setText("Failed to load: " + e.getMessage())
                );
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
