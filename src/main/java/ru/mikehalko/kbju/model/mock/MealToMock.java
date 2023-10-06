package ru.mikehalko.kbju.model.mock;

import ru.mikehalko.kbju.model.meal.Nutritionally;
import ru.mikehalko.kbju.to.MealTo;

public class MealToMock extends MealTo {
    public MealToMock() {
        super(new Nutritionally());
    }
}
