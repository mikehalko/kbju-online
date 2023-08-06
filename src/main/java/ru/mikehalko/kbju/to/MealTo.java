package ru.mikehalko.kbju.to;

import ru.mikehalko.kbju.model.Meal;
import ru.mikehalko.kbju.model.Nutritionally;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        return "MEAL-TO (" + (excess ? "TRUE!" : "false") + ") "
                + dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                + " | " + nutritionally + " |"
                + " | " + mass + " |"
                + " | " + description + " |";
    }
}
