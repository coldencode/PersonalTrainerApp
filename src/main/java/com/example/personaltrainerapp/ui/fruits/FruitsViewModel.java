package com.example.personaltrainerapp.ui.fruits;

import com.example.personaltrainerapp.model.Fruit;
import com.example.personaltrainerapp.services.FruitApiService;
import com.example.personaltrainerapp.services.FruityViceAPIService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * View model to handle the Fruits page
 */
public class FruitsViewModel {

    private final FruitApiService apiService;
    private final ObservableList<Fruit> fruits = FXCollections.observableArrayList();

    public FruitsViewModel(FruitApiService apiService) {
        this.apiService = apiService;
    }

    /** Getter to retrieve the list of fruits */
    public ObservableList<Fruit> getFruits() {
        return fruits;
    }

    /** Loads all the fruits from the service*/
    public void loadFruits() throws Exception {
        fruits.setAll(apiService.fetchAll());
    }
}
