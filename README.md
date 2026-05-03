# Personal Trainer App

A JavaFX desktop app for tracking workouts, push-ups, calories, and weight.
With integration with FruityViceAPI to retrieve fruit nutritional information!

Made by Ching Wei Choi

## Requirements

- Java 17+
- Maven 3.8+

## How to Run

Go to project folder where ``pom.xml`` is located.
and run the command:
```bash
mvn clean javafx:run
```

## First Launch

On first launch you'll be taken through a short onboarding flow to set up your profile (name, goal, height, weight, date of birth). After that you'll land on the main dashboard.

## Tabs

| Tab | What it does |
|---|---|
| Dashboard | Daily calorie goal, BMI, weight chart |
| Workouts | Log workouts, weekly stats, personal bests |
| Push-Ups | Log push-ups, compete against a friend |
| Fruits | Browse fruit nutrition data from the Fruityvice API |

## Project Structure

```
src/main/java/com/example/personaltrainerapp/
├── Main.java                  # Entry point
├── AppContext.java            # Shared dependencies (DB connection, repositories)
├── SceneManager.java          # Builds the main tab view
├── database/                  # DatabaseManager, schema initialisation
├── model/                     # User, entry records, push-up buddy models
├── repository/                # SQL access for each data type
├── services/                  # FruitApiService interface + implementations
└── ui/                        # Controllers and ViewModels per tab
```
