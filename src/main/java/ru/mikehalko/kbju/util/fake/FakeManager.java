package ru.mikehalko.kbju.util.fake;

import ru.mikehalko.kbju.repository.fake.FakeMealRepository;

public class FakeManager {
    private FakeMealRepository repository;
    private FakeBuilder mealBuilder;

    public FakeManager(FakeMealRepository repository) {
        this.repository = repository;
        this.mealBuilder = repository.getMealBuilder();
    }

    public String getTextContentRepository() {
        return repository.getContentText();
    }

    public void resetRepository() {
        repository.reset();
    }


    public FakeMealRepository getRepository() {
        return repository;
    }

    public FakeBuilder getFakeBuilder() {
        return mealBuilder;
    }

    public void setRepository(FakeMealRepository repository) {
        this.repository = repository;
    }

    public void setFakeBuilder(FakeBuilder mealBuilder) {
        this.mealBuilder = mealBuilder;
    }


    public void setNumberOfDays(int number) {
        repository.setDays(number);
    }

    public void setMealsPerDay(int number) {
        repository.setMealsPerDay(number);
    }
}
