package ru.mikehalko.kbju.controller;


import ru.mikehalko.kbju.service.MealService;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.to.MealTo;
import ru.mikehalko.kbju.util.*;

import java.util.List;

public class MealController {

    private MealService service;

    public MealController(MealService service) {
        this.service = service;
    }

    public MealTo get(int id) {
        int userId = SecurityUtil.authId();
        return MealsUtil.getTo(service.get(id, userId));
    }

    public void delete(int id) {
        int userId = SecurityUtil.authId();
        service.delete(id, userId);
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authId();
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.nutritionalValue());
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authId();
        return service.create(meal, userId);
    }

    public void update(Meal meal, int id) {
        meal.setId(id);
        int userId = SecurityUtil.authId();
        service.update(meal, userId);
    }
}