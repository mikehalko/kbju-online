package ru.mikehalko.kbju.model;

import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.meal.Nutritionally;

import java.util.List;

public class User {
    private int id;
    private Nutritionally nutritionally; // norm per day
    private String name;

    public User() {
    }

    public User(int id, Nutritionally nutritionally, String name) {
        this.id = id;
        this.nutritionally = nutritionally;
        this.name = name;
    }

    public User(int id, Nutritionally nutritionally, String name, List<Meal> meals) {
        this.id = id;
        this.nutritionally = nutritionally;
        this.name = name;
        this.meals = meals;
    }

    public int getId() {
        return id;
    }

    private List<Meal> meals;

    public void setId(int id) {
        this.id = id;
    }

    public Nutritionally getNutritionally() {
        return nutritionally;
    }

    public void setNutritionally(Nutritionally nutritionally) {
        this.nutritionally = nutritionally;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public String toString() {
        return String.format("USER[%3d]", id);
    }

    public boolean isNew() {
        return id == 0;
    }
}
