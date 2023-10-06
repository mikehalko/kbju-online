package ru.mikehalko.kbju.model.mock;


import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.user.User;

import java.util.List;

public class UserMock extends User {
    private final String EXCEPTION_MESSAGE = "mock object";

    @Override
    public void setId(int id) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setMeals(List<Meal> meals) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setCaloriesMin(int caloriesMin) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setCaloriesMax(int caloriesMax) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean isNew() {
        return true;
    }

    @Override
    public void setRole(Role role) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }
}
