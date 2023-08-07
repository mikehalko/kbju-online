package ru.mikehalko.kbju.util;

import ru.mikehalko.kbju.model.Meal;
import ru.mikehalko.kbju.model.Nutritionally;
import ru.mikehalko.kbju.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealsUtil {
    public static List<MealTo> getTos(List<Meal> meals, Nutritionally nutritionallyPerDay) {
        return filtered(meals, LocalTime.MIN, LocalTime.MAX, nutritionallyPerDay.getCalories());
    }

    
    public static List<MealTo> filtered(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // map с суммой калорий по дням
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate,
                         Collectors.summingInt(meal -> meal.getNutritionally().getCalories()))
                );

        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal, excess);
    }

    public static MealTo getTo(Meal meal) {
        return createTo(meal, false);
    }

    public static int calculateCalories (int proteins, int fats, int carbohydrates) {
        return proteins * 4  + fats * 9 + carbohydrates * 4;
    }
}