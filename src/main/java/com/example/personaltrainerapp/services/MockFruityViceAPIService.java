package com.example.personaltrainerapp.services;

import com.example.personaltrainerapp.model.Fruit;

import java.util.List;

/**
 * A Mock API implementation of the FruityViceAPIService for testing purposes
 */
public class MockFruityViceAPIService implements FruitApiService {

    /**
     * Fetch a list of Fruit objects that are instantiated during runtime
     * @return a list of fruit objects
     */
    @Override
    public List<Fruit> fetchAll() {
        return List.of(
                new Fruit("Apple", "Rosaceae",     "Malus",       52,  0.4, 10.3, 11.4, 0.3),
                new Fruit("Banana", "Musaceae",     "Musa",        96,  0.2, 17.2, 22.0, 1.0),
                new Fruit("Blueberry","Ericaceae",    "Vaccinium",   57,  0.3,  9.7, 14.5, 0.7),
                new Fruit("Cherry", "Rosaceae",     "Prunus",      50,  0.3, 12.8, 12.2, 1.0),
                new Fruit("Mango",      "Anacardiaceae","Mangifera",   60,  0.4, 13.7, 15.0, 0.8),
                new Fruit("Orange",     "Rutaceae",     "Citrus",      43,  0.2,  8.5,  8.3, 1.0),
                new Fruit("Pear",       "Rosaceae",     "Pyrus",       57,  0.1,  9.8, 15.2, 0.4),
                new Fruit("Pineapple",  "Bromeliaceae", "Ananas",      50,  0.1,  9.9, 13.1, 0.5),
                new Fruit("Strawberry", "Rosaceae",     "Fragaria",    29,  0.4,  4.9,  5.5, 0.8),
                new Fruit("Watermelon", "Cucurbitaceae","Citrullus",   30,  0.2,  6.0,  7.6, 0.6)
        );
    }
}
