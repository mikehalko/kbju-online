package ru.mikehalko.kbju.util.model;

import ru.mikehalko.kbju.model.user.User;

public class UserUtil {
    public static User createUser(int id, String name, int caloriesMin, int caloriesMax) {
        return new User(id, name, caloriesMin, caloriesMax);
    }

    public static User clone(User user) {
        User clone = new User(user.getId(), user.getName(), user.getCaloriesMin(), user.getCaloriesMax(), user.getMeals());

        return clone;
    }

    public static User[] clone(User... users) {
        User[] cloneUsers = new User[users.length];
        for (int i = 0; i < cloneUsers.length; i++) {
            cloneUsers[i] = clone(users[i]);
        }
        return cloneUsers;
    }
}
