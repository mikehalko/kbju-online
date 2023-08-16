package ru.mikehalko.kbju.util;

import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.model.meal.Nutritionally;

public class UserUtil {
    public static User cloneUser(User user) {
        Nutritionally nut = user.getNutritionally();
        Nutritionally nutritionallyClone =
                new Nutritionally(nut.getProteins(), nut.getFats(), nut.getCarbohydrates(), nut.getCalories());
        User clone = new User(user.getId(), nutritionallyClone, user.getName());
        clone.setMeals(user.getMeals());

        return clone;
    }
}
