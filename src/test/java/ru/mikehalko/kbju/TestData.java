package ru.mikehalko.kbju;

import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.meal.Nutritionally;

import java.time.LocalDateTime;

public class TestData {
    public static final User USER_1 = new User(0, new Nutritionally(200, 300, 200, 2700), "USER_1");
    public static final User USER_2 = new User(0, new Nutritionally(250, 350, 250, 3200), "USER_2");

    public static final Meal MEAL_1 = new Meal(0, null, LocalDateTime.now(), 100, "MEAL_1", new Nutritionally(5, 2, 4, 54));
    public static final Meal MEAL_2 = new Meal(0, null, LocalDateTime.now(), 100, "MEAL_2", new Nutritionally(1, 23, 45, 391));
    public static final Meal MEAL_3 = new Meal(0, null, LocalDateTime.now(), 100, "MEAL_3", new Nutritionally(1, 2, 3, 34));
    public static final Meal MEAL_4 = new Meal(0, null, LocalDateTime.now(), 100, "MEAL_4", new Nutritionally(10, 14, 16, 230));
    public static final Meal MEAL_5 = new Meal(0, null, LocalDateTime.now(), 100, "MEAL_5", new Nutritionally(2, 10, 234, 1034));
    public static final Meal MEAL_6 = new Meal(0, null, LocalDateTime.now(), 100, "MEAL_6", new Nutritionally(100, 111, 134, 1987));
    public static final Meal MEAL_7 = new Meal(0, null, LocalDateTime.now(), 100, "MEAL_7", new Nutritionally(120, 100, 204, 2241));
}
