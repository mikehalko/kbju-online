package ru.mikehalko.kbju.controller;


import ru.mikehalko.kbju.service.MealService;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.to.MealTo;
import ru.mikehalko.kbju.util.model.MealsUtil;
import ru.mikehalko.kbju.util.security.SecurityUtil;

import java.util.List;

public class MealController {

    private final MealService service;
    private static MealController instance;

    private MealController(MealService service) {
        this.service = service;
    }

    public static synchronized MealController getInstance(MealService service) {
        if (instance == null) {
            instance = new MealController(service);
        }
        return instance;
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
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.caloriesMax(), SecurityUtil.caloriesMin());
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