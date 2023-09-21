package ru.mikehalko.kbju.util.security;

import ru.mikehalko.kbju.model.user.User;

public class SecurityUtil {
    private static User user;

    public static int authId() {
        return user.getId();
    }

    public static int authCaloriesPerDay() {
        checkUser();
        return user.getCaloriesMax();
    }

    public static int caloriesMin() {
        checkUser();
        return user.getCaloriesMin();
    }

    public static int caloriesMax() {
        checkUser();
        return user.getCaloriesMax();
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