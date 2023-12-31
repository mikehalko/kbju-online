package ru.mikehalko.kbju.model.meal;

import java.util.Objects;

public class Nutritionally {
    private int proteins;
    private int fats;
    private int carbohydrates;
    private int calories;

    public Nutritionally() {

    }

    public Nutritionally(int proteins, int fats, int carbohydrates, int calories) {
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
    }

    public int getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String toString() {
        return String.format("{ %4d | %4d | %4d | %4d }", proteins, fats, carbohydrates, calories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nutritionally that = (Nutritionally) o;
        return proteins == that.proteins && fats == that.fats && carbohydrates == that.carbohydrates && calories == that.calories;
    }

    @Override
    public int hashCode() {
        return Objects.hash(proteins, fats, carbohydrates, calories);
    }
}
