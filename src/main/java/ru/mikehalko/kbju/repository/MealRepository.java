package ru.mikehalko.kbju.repository;

import ru.mikehalko.kbju.model.meal.Meal;

import java.util.List;

public interface MealRepository {
    Meal save(Meal meal, int userId);

    boolean delete(int id, int userId);

    Meal get(int id, int userId);

    List<Meal> getAll(int userId);
}
