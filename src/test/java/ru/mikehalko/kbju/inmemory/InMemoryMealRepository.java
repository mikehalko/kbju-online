package ru.mikehalko.kbju.inmemory;

import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.repository.MealRepository;
import ru.mikehalko.kbju.util.security.SecurityUtil;

import static ru.mikehalko.kbju.inmemory.InMemoryExceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private final static int DEFAULT_ID_START = 100;
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(DEFAULT_ID_START);

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(idCounter.getAndIncrement());
            meal.setUser(SecurityUtil.getUser());
            getMealsOrAssignNew(SecurityUtil.getUser()).add(meal);
            mealsMap.put(meal.getId(), meal);
        } else {
            // update
            Meal oldMeal = mealsMap.remove(meal.getId());
            checkMeal(oldMeal, meal.getId(), userId);

            mealsMap.put(meal.getId(), meal);
            getMealsOrAssignNew(meal.getUser()).add(meal);
        }

        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal removable = mealsMap.get(id);
        checkMeal(removable, id, userId);

        getMealsOrAssignNew(removable.getUser()).remove(removable);

        return mealsMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = mealsMap.get(id);
        checkMeal(meal, id, userId);

        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealsMap.values().stream().collect(Collectors.toList());
    }

    private List<Meal> getMealsOrAssignNew(User user) {
        List<Meal> userMeals = user.getMeals();
        if (userMeals == null) {
            userMeals = new ArrayList<>();
            user.setMeals(userMeals);
        }

        return userMeals;
    }

    private void checkMeal(Meal meal, int mealId, int ownerId) {
        if (meal == null)
            throw new NotFoundException("not found meal with id=" + mealId);
        if (meal.getUser().getId() != ownerId)
            throw new UserNotOwnException("belongs to another owner with id" + meal.getUser().getId());
    }

    public void reset() {
        mealsMap.clear();
        idCounter.set(DEFAULT_ID_START);
    }
    public void initForUser(User user, Meal... meals) {
        List<Meal> userMeals = new ArrayList<>(List.of(meals));
        user.setMeals(userMeals);
        for (Meal meal : meals) {
            meal.setUser(user);
            userMeals.add(meal);
            meal.setId(idCounter.getAndIncrement());
            mealsMap.put(meal.getId(), meal);
        }
    }
}
