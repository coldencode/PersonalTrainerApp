package com.example.personaltrainerapp.ui.fruits;

import com.example.personaltrainerapp.model.Fruit;
import com.example.personaltrainerapp.services.FruitApiService;
import com.example.personaltrainerapp.services.FruityViceAPIService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FruitsViewModel {

    private final FruitApiService apiService;
    private final ObservableList<Fruit> fruits = FXCollections.observableArrayList();

    public FruitsViewModel() {
        this(new FruityViceAPIService());
    }

    public FruitsViewModel(FruitApiService apiService) {
        this.apiService = apiService;
    }

    public ObservableList<Fruit> getFruits() {
        return fruits;
    }

    public void loadFruits() throws Exception {
        fruits.setAll(apiService.fetchAll());
    }
}
