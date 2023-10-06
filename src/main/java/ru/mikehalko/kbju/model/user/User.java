package ru.mikehalko.kbju.model.user;

import ru.mikehalko.kbju.model.meal.Meal;

import java.util.List;
import java.util.Objects;

public class User {
    private int id;
    private Role role;
    private String name;
    private int caloriesMin; // min norm per day
    private int caloriesMax; // max norm per day
    private List<Meal> meals;

    public User() {

    }

    public User(int id, String name, int caloriesMin, int caloriesMax) {
        this.id = id;
        this.name = name;
        this.caloriesMin = caloriesMin;
        this.caloriesMax = caloriesMax;
    }

    public User(int id, String name, int caloriesMin, int caloriesMax, List<Meal> meals) {
        this.id = id;
        this.name = name;
        this.caloriesMin = caloriesMin;
        this.caloriesMax = caloriesMax;
        this.meals = meals;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCaloriesMin() {
        return caloriesMin;
    }

    public void setCaloriesMin(int caloriesMin) {
        this.caloriesMin = caloriesMin;
    }

    public int getCaloriesMax() {
        return caloriesMax;
    }

    public void setCaloriesMax(int caloriesMax) {
        this.caloriesMax = caloriesMax;
    }

    public String toString() {
        return String.format("%s[%3d] \"%s\"|%s4d|%s4d|", role, id, name, caloriesMin, caloriesMax);
    }

    public boolean isNew() {
        return id == 0;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && caloriesMin == user.caloriesMin && caloriesMax == user.caloriesMax && role == user.role && Objects.equals(name, user.name) && Objects.equals(meals, user.meals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, name, caloriesMin, caloriesMax, meals);
    }

    public enum Role {
        ADMIN, USER, UNKNOWN
    }
}
