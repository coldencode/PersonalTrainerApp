package com.example.personaltrainerapp;

import com.example.personaltrainerapp.repository.*;
import com.example.personaltrainerapp.services.FruitApiService;
import com.example.personaltrainerapp.services.FruityViceAPIService;

import java.sql.Connection;

/**
 * Holds all shared application-level resources.
 * Created once in Main and passed to SceneManager — nothing below this
 * class needs to know how dependencies are constructed.
 */
public class AppContext {

    public final UserRepository    userRepo;
    public final WeightRepository  weightRepo;
    public final MealRepository    mealRepo;
    public final WorkoutRepository workoutRepo;
    public final PushUpRepository  pushUpRepo;
    public final FriendRepository  friendRepo;
    public final FruitApiService   fruitApiService;

    public AppContext(Connection conn, FruitApiService fruitApiService) {
        this.userRepo        = new UserRepository(conn);
        this.weightRepo      = new WeightRepository(conn);
        this.mealRepo        = new MealRepository(conn);
        this.workoutRepo     = new WorkoutRepository(conn);
        this.pushUpRepo      = new PushUpRepository(conn);
        this.friendRepo      = new FriendRepository(conn);
        this.fruitApiService = fruitApiService;
    }
}
