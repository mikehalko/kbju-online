package ru.mikehalko.kbju.util;

import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.model.Nutritionally;

public class SecurityUtil {
    private static User user;

    public static int authId() {
        return user.getId();
    }

    public static int authCaloriesPerDay() {
        return user.getNutritionallyNorm().getCalories();
    }

    public static Nutritionally nutritionalValue() {
        return user.getNutritionally();
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        SecurityUtil.user = user;
    }
}