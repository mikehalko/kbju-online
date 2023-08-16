package ru.mikehalko.kbju.util;

import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.model.meal.Nutritionally;

public class SecurityUtil {
    private static User user;

    public static int authId() {
        return user.getId();
    }

    public static int authCaloriesPerDay() {
        checkUser();
        return user.getNutritionally().getCalories();
    }

    public static Nutritionally nutritionalValue() {
        checkUser();
        return user.getNutritionally();
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        SecurityUtil.user = user;
    }

    private static void checkUser() {
        if (user == null) throw new RuntimeException("no authorized users");
    }
}