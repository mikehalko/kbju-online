package ru.mikehalko.kbju.util;

import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.model.Nutritionally;
import ru.mikehalko.kbju.util.fake.FakeAuthUser;

public class SecurityUtil {
    public static int authId() {
        return FakeAuthUser.id();
    }

    public static int authCaloriesPerDay() {
        return FakeAuthUser.caloriesPerDay();
    }

    public static Nutritionally nutritionalValue() {
        return FakeAuthUser.nutritionalValue();
    }

    public static User user() {
        return FakeAuthUser.user();
    }
}