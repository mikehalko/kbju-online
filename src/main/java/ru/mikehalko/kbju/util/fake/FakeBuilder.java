package ru.mikehalko.kbju.util.fake;

import ru.mikehalko.kbju.model.Meal;
import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.model.Nutritionally;
import ru.mikehalko.kbju.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Random;

public class FakeBuilder {
    public Meal randomMeal(User owner, LocalDateTime dateTime, Random random) {
        int multiplier = 2 + random.nextInt(15);
        int proteins = random.nextInt(5 * multiplier);
        int fats = random.nextInt(10 * multiplier);
        int carbohydrates = random.nextInt(30 * multiplier);
        int mass = (proteins + fats + carbohydrates) * (1 + random.nextInt(2));
        Nutritionally nutritionally = nutritionally(proteins, fats, carbohydrates);

        return meal(owner, dateTime, mass, "random food", nutritionally);
    }

    public Meal meal(User user, LocalDateTime dateTime, int mass, String descr, Nutritionally nutritionally) {
        Meal meal = new Meal();
        meal.setDateTime(dateTime);
        meal.setUser(user);
        meal.setMass(mass);
        meal.setDescription(descr);
        meal.setNutritionally(nutritionally);

        return meal;
    }

    public Meal meal(int id, User user, LocalDateTime dateTime, int mass, String descr, Nutritionally nut) {
        Meal meal = meal(user, dateTime, mass, descr, nut);
        meal.setId(id);
        return meal;
    }

    public Meal copyMeal(int id, Meal meal) {
        Meal mealCopy = new Meal();
        mealCopy.setId(id);
        mealCopy.setUser(meal.getUser());
        mealCopy.setDateTime(meal.getDateTime());
        mealCopy.setMass(meal.getMass());
        mealCopy.setDescription(meal.getDescription());
        mealCopy.setNutritionally(meal.getNutritionally());

        return mealCopy;
    }

    public Nutritionally nutritionally(int proteins, int fats, int carbohydrates) {
        Nutritionally nutritionally = new Nutritionally();
        nutritionally.setProteins(proteins);
        nutritionally.setFats(fats);
        nutritionally.setCarbohydrates(carbohydrates);
        nutritionally.setCalories(MealsUtil.calculateCalories(proteins, fats, carbohydrates));

        return nutritionally;
    }
}