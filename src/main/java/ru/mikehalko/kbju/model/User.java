package ru.mikehalko.kbju.model;

import java.util.List;

public class User {
    private int id;
    private Nutritionally nutritionally; // norm per day
    private String name;

    public int getId() {
        return id;
    }

    private List<Meal> meals;

    public void setId(int id) {
        this.id = id;
    }

    public Nutritionally getNutritionallyNorm() {
        return nutritionally;
    }

    public void setNutritionallyNorm(Nutritionally nutritionally) {
        this.nutritionally = nutritionally;
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
}
