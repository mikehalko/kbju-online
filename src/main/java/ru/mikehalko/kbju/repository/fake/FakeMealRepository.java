package ru.mikehalko.kbju.repository.fake;

import ru.mikehalko.kbju.model.Meal;
import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.repository.MealRepository;
import ru.mikehalko.kbju.util.fake.FakeBuilder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FakeMealRepository implements MealRepository {
    private static final int DEFAULT_ID_COUNTER_VALUE = 100;
    private final AtomicInteger nextIdCounter = new AtomicInteger(DEFAULT_ID_COUNTER_VALUE);

    private int days = 6;
    private int mealsPerDay = 3;
    private User initUser;

    // TODO: no thread safe!
    private final List<Meal> meals;
    private final FakeBuilder mealBuilder;


    public FakeMealRepository(FakeBuilder mealBuilder) {
        this.mealBuilder = mealBuilder;
        meals = new ArrayList<>();
    }

    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) { // save
            meal.setId(nextIdCounter.getAndIncrement());
            meals.add(meal);
        }
        else { // update
            if (delete(meal.getId(), userId)) {
                meals.add(meal);
            } else {
                throw new RuntimeException("meal update: id does not exist OR userId is not the owner");
            }
        }
        return meal;
    }

    public boolean delete(int id, int userId) {
        return meals.removeIf(meal -> meal.getId() == id && meal.getUser().getId() == userId);
    }

    public Meal get(int id, int userId) {
        Optional<Meal> optionalMeal = meals.stream()
                .filter(meal -> meal.getId() == id)
                .findAny();

        if (optionalMeal.isPresent()) {
            Meal result = optionalMeal.get();
            if (result.getUser().getId() == userId)
                return result;
            else
                throw new RuntimeException("userId didn't match");
        } else
            throw  new RuntimeException("the meal does not exist");
    }

    public List<Meal> getAll(int userId) {
        return meals.stream()
                .filter(meal -> meal.getUser().getId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public FakeBuilder getMealBuilder() {
        return mealBuilder;
    }

    public void init() {
        resetIdCounter();
        Random random = new Random(24); // обеспечить те же данные, при условии, что random никем не используется
        for (int i = 0; i < days; i++) {
            for (int j = 0; j < mealsPerDay; j++) {
                save(mealBuilder.randomMeal(initUser, LocalDateTime.now().minusDays(i), random), initUser.getId());
            }
        }
    }

    public void reset() {
        meals.clear();
        init();
    }

    public String getContentText() {
        StringBuilder builder = new StringBuilder();
        meals.forEach((meal) -> {
            builder.append(meal);
            builder.append("\n");
        });
        return builder.toString();
    }

    public void setRepositoryInitUserOwner(User initUser) {
        this.initUser = initUser;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setMealsPerDay(int mealsPerDay) {
        this.mealsPerDay = mealsPerDay;
    }

    private void resetIdCounter() {
        this.nextIdCounter.set(DEFAULT_ID_COUNTER_VALUE);
    }
}
