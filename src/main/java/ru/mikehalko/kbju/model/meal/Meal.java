package ru.mikehalko.kbju.model.meal;

import ru.mikehalko.kbju.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Meal {
    private int id;
    private User user; // owner
    private LocalDateTime dateTime;

    private int mass;
    private String description;
    private Nutritionally nutritionally;

    public Meal(){}

    public Meal(int id, User user, LocalDateTime dateTime, int mass, String description, Nutritionally nutritionally) {
        this.id = id;
        this.user = user;
        this.dateTime = dateTime;
        this.mass = mass;
        this.description = description;
        this.nutritionally = nutritionally;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Nutritionally getNutritionally() {
        return nutritionally;
    }

    public void setNutritionally(Nutritionally nutritionally) {
        this.nutritionally = nutritionally;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public boolean isNew() {
        return id == 0;
    }

    public String toString() {
        return String.format("MEAL [%3d] by user \"%s\" %s, || %s   %4d g  \"%s\"", id, user,
                dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), nutritionally, mass, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return id == meal.id && mass == meal.mass && Objects.equals(user, meal.user) && Objects.equals(dateTime, meal.dateTime) && Objects.equals(description, meal.description) && Objects.equals(nutritionally, meal.nutritionally);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, dateTime, mass, description, nutritionally);
    }
}
