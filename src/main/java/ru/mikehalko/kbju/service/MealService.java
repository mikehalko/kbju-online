package ru.mikehalko.kbju.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.repository.MealRepository;

import java.util.List;

public class MealService {
    private Logger log = LoggerFactory.getLogger(MealService.class);

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal get(int id, int userId) {
        Meal meal = repository.get(id, userId);
        log.debug("get by id={}: {}", userId, meal);
        return meal;
    }

    public void delete(int id, int userId) {
        boolean result = repository.delete(id, userId);
        log.debug("delete by id={}: meal id={}, return = {}", userId, id, result);
    }

    public List<Meal> getAll(int userId) {
        List<Meal> result = repository.getAll(userId);
        log.debug("get-all: {}", result);

        return result;
    }

    public void update(Meal meal, int userId) {
        Meal updated = repository.save(meal, userId);
        log.debug("update by id={}: {}, return = {}", userId, meal, updated);
    }

    public Meal create(Meal meal, int userId) {
        Meal saved = repository.save(meal, userId);
        log.debug("save by id={}: {}, return = {}", userId, meal, saved);
        return saved;
    }
}