package ru.mikehalko.kbju.util.model;

import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.meal.Nutritionally;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.to.MealTo;
import ru.mikehalko.kbju.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealsUtil {

    public static Meal createMeal(int id, User user, LocalDateTime dateTime, int mass, String description,
                                  int proteins, int fats, int carbohydrates, int calories) {
        if (calories == 0) calories = calculateCalories(proteins, fats, carbohydrates);
        Nutritionally nutritionally = new Nutritionally(proteins, fats, carbohydrates, calories);
        return new Meal(id, user, dateTime, mass, description, nutritionally);
    }

    public static List<MealTo> getTos(List<Meal> meals, int calories_min, int calories_max) {
        return filtered(meals, LocalTime.MIN, LocalTime.MAX, calories_min, calories_max);
    }

    public static List<MealTo> filtered(List<Meal> meals, LocalTime startTime, LocalTime endTime,
                                        int caloriesMinPerDay, int caloriesMaxPerDay) {
        // map с суммой калорий по дням
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate,
                         Collectors.summingInt(meal -> meal.getNutritionally().getCalories()))
                );

        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal,
                        caloriesSumByDate.get(meal.getDate()) < caloriesMinPerDay,
                        caloriesSumByDate.get(meal.getDate()) > caloriesMaxPerDay))
                .sorted(MealsUtil::sortOrderByDateTimeDesc)
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean shortage, boolean excess) {
        return new MealTo(meal, shortage, excess);
    }

    public static MealTo getTo(Meal meal) {
        return createTo(meal, false, false);
    }

    public static int calculateCalories (int proteins, int fats, int carbohydrates) {
        return proteins * 4  + fats * 9 + carbohydrates * 4;
    }

    public static Meal clone(Meal meal) {
        Meal clone = new Meal(meal.getId(), meal.getUser(), meal.getDateTime(), meal.getMass(),  meal.getDescription(),
                new Nutritionally(meal.getNutritionally().getProteins(),
                        meal.getNutritionally().getFats(),
                       meal.getNutritionally().getCarbohydrates(),
                        meal.getNutritionally().getCalories()));

        return clone;
    }

    public static Meal[] clone(Meal... meals) {
        Meal[] cloneMeals = new Meal[meals.length];
        for (int i = 0; i < cloneMeals.length; i++) {
            cloneMeals[i] = clone(meals[i]);
        }
        return cloneMeals;
    }

    public static int sortOrderByDateTimeDesc(MealTo m1, MealTo m2) {
        return m1.getDateTime().compareTo(m2.getDateTime()) * -1;
    }
}
