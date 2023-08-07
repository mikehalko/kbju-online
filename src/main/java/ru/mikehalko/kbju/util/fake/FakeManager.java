package ru.mikehalko.kbju.util.fake;

import ru.mikehalko.kbju.model.Nutritionally;
import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.repository.MealRepository;
import ru.mikehalko.kbju.repository.fake.FakeMealRepository;
import ru.mikehalko.kbju.util.MealsUtil;

public class FakeManager {
    private FakeMealRepository repository;
    private FakeBuilder mealBuilder;

    public FakeManager() {
        this.mealBuilder = new FakeBuilder();
        this.repository = new FakeMealRepository(mealBuilder);
        repository.setRepositoryInitUserOwner(getFakeAuthUser()); // default
    }

    public MealRepository getRepository() {
        return repository;
    }

    public FakeBuilder fakeBuilder() {
        return mealBuilder;
    }

    public String getContentRepositoryInText() {
        return repository.getContentText();
    }

    public void resetRepository() {
        repository.reset();
    }

    public void initRepository() {
        repository.reset();
    }

    public void setNumberOfDays(int number) {
        repository.setDays(number);
    }

    public void setMealsPerDay(int number) {
        repository.setMealsPerDay(number);
    }

    public User getFakeAuthUser() {
        return FakeUser.get();
    }

    public void setUserOwnerMeals(User user) {
        repository.setRepositoryInitUserOwner(user);
    }

    private static class FakeUser {
        private final static User USER = new User();
        private final static String DEFAULT_NAME = "S0me_User";
        private final static int DEFAULT_ID = 1;
        private final static int DEFAULT_PROTEINS = 204;
        private final static int DEFAULT_FATS = 91;
        private final static int DEFAULT_CARBOHYDRATES = 272;
        private final static int DEFAULT_CALORIES = MealsUtil.calculateCalories(DEFAULT_PROTEINS, DEFAULT_FATS, DEFAULT_CARBOHYDRATES);
        static {
            USER.setId(DEFAULT_ID);
            USER.setName(DEFAULT_NAME);
            USER.setMeals(null);
            Nutritionally nutVal = new Nutritionally();
            nutVal.setCalories(DEFAULT_CALORIES);
            nutVal.setProteins(DEFAULT_PROTEINS);
            nutVal.setFats(DEFAULT_FATS);
            nutVal.setCarbohydrates(DEFAULT_CARBOHYDRATES);
            USER.setNutritionallyNorm(nutVal);
        }

        public static User get() {
            return USER;
        }
    }
}
