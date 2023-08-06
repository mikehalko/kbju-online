package ru.mikehalko.kbju.service;

import ru.mikehalko.kbju.model.Meal;
import ru.mikehalko.kbju.repository.MealRepository;

import java.util.List;

public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal get(int id, int userId) {
        return repository.get(id, userId);
    }

    public void delete(int id, int userId) {
        repository.delete(id, userId);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public void update(Meal meal, int userId) {
        repository.save(meal, userId);
    }

    public Meal create(Meal Meal, int userId) {
        return repository.save(Meal, userId);
    }
}