package ru.mikehalko.kbju.to;

import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.meal.Nutritionally;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MealTo {
    private int id;
    private LocalDateTime dateTime;
    private boolean excess;

    private final int mass;
    private final String description;
    private final Nutritionally nutritionally;



    public MealTo(Meal meal, boolean excess) {
        this.dateTime = meal.getDateTime();
        this.id = meal.getId();
        this.mass = meal.getMass();
        this.description = meal.getDescription();
        this.nutritionally = meal.getNutritionally();
        this.excess = excess;
    }

    public int getId() {
        return id;
    }

    public boolean isExcess() {
        return excess;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getMass() {
        return mass;
    }

    public int getCalories() {
        return nutritionally.getCalories();
    }

    public int getProteins() {
        return nutritionally.getProteins();
    }

    public int getFats() {
        return nutritionally.getFats();
    }

    public int getCarbohydrates() {
        return nutritionally.getCarbohydrates();
    }

    public String toString() {
        return String.format("MEAL-TO[%3d] (%s) %s, | %s  %4d g  \"%s\"", id, excess,
                dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), nutritionally, mass, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealTo mealTo = (MealTo) o;
        return id == mealTo.id && excess == mealTo.excess && mass == mealTo.mass && Objects.equals(dateTime, mealTo.dateTime) && Objects.equals(description, mealTo.description) && Objects.equals(nutritionally, mealTo.nutritionally);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, excess, mass, description, nutritionally);
    }
}