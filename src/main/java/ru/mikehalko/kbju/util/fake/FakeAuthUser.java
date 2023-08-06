package ru.mikehalko.kbju.util.fake;

import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.model.Nutritionally;
import ru.mikehalko.kbju.util.MealsUtil;


public class FakeAuthUser {
    private final static User USER = new User();
    private final static String DEFAULT_NAME = "S0me_User";
    private final static int DEFAULT_PROTEINS = 204;
    private final static int DEFAULT_FATS = 91;
    private final static int DEFAULT_CARBOHYDRATES = 272;
    private final static int DEFAULT_CALORIES = MealsUtil.calculateCalories(DEFAULT_PROTEINS, DEFAULT_FATS, DEFAULT_CARBOHYDRATES);
    static {
        USER.setId(1);

        Nutritionally nutVal = new Nutritionally();
        nutVal.setCalories(DEFAULT_CALORIES);
        nutVal.setProteins(DEFAULT_PROTEINS);
        nutVal.setFats(DEFAULT_FATS);
        nutVal.setCarbohydrates(DEFAULT_CARBOHYDRATES);
        USER.setNutritionallyNorm(nutVal);
        USER.setName(DEFAULT_NAME);
        USER.setMeals(null);
    }

    public static int id() {
        return USER.getId();
    }

    public static int caloriesPerDay() {
        return USER.getNutritionallyNorm().getCalories();
    }

    protected static int proteinsPerDay() {
        return USER.getNutritionallyNorm().getProteins();
    }

    protected static int fatsPerDay() {
        return USER.getNutritionallyNorm().getFats();
    }

    protected static int carbohydratesPerDay() {
        return USER.getNutritionallyNorm().getCarbohydrates();
    }

    public static Nutritionally nutritionalValue() {
        return USER.getNutritionallyNorm();
    }

    public static User user() {
        return USER;
    }
}
