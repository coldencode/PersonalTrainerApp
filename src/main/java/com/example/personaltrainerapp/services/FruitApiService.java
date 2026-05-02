package com.example.personaltrainerapp.services;

import com.example.personaltrainerapp.model.Fruit;

import java.util.List;

public interface FruitApiService {
    List<Fruit> fetchAll() throws Exception;
}
