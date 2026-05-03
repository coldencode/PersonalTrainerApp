package com.example.personaltrainerapp.services;

import com.example.personaltrainerapp.model.Fruit;

import java.util.List;

/**
 * Interface to define the Fruit API Service functions
 */
public interface FruitApiService {
    List<Fruit> fetchAll() throws Exception;
}
