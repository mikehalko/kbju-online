package ru.mikehalko.kbju.controller;

import org.junit.*;
import ru.mikehalko.kbju.inmemory.InMemoryExceptions.*;
import ru.mikehalko.kbju.inmemory.InMemoryMealRepository;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.service.MealService;
import ru.mikehalko.kbju.to.MealTo;
import ru.mikehalko.kbju.util.model.MealsUtil;
import ru.mikehalko.kbju.util.security.SecurityUtil;
import ru.mikehalko.kbju.util.model.UserUtil;

import java.util.List;

import static ru.mikehalko.kbju.data.TestData.*;
import static ru.mikehalko.kbju.util.TestUtil.prepareDataMeal;
import static ru.mikehalko.kbju.util.TestUtil.prepareDataMealTos;

public class InMemoryMealControllerTest {

    private static MealController controller;
    private static InMemoryMealRepository repository;

    @BeforeClass
    public static void beforeClass() {
        repository = new InMemoryMealRepository();
        controller = MealController.getInstance(MealService.getInstance(repository));
    }

    @AfterClass
    public static void afterClass() {
        controller = null;
        repository.reset();
        repository = null;
        SecurityUtil.setUser(null);
    }

    @Before
    public void setUp() throws Exception {
        SecurityUtil.setUser(UserUtil.clone(USER_2));
        User testUser = SecurityUtil.getUser();
        testUser.setId(101);
        repository.reset();
        repository.initForUser(testUser, MealsUtil.clone(MEAL_1, MEAL_2, MEAL_3, MEAL_4, MEAL_5));
    }

    @Test
    public void getAll() {
        List<MealTo> expectedMealTos = prepareDataMealTos(SecurityUtil.caloriesMin(), SecurityUtil.caloriesMax(),
                100, MEAL_1, MEAL_2, MEAL_3, MEAL_4, MEAL_5);
        Assert.assertEquals(expectedMealTos, controller.getAll());
    }

    @Test
    public void get() {
        int id = 100;
        Meal expected = prepareDataMeal(MEAL_1, id, SecurityUtil.getUser());
        Assert.assertEquals(MealsUtil.getTo(expected), controller.get(id));
    }

    @Test()
    public void delete() {
        int idDeleted = 100;
        controller.delete(idDeleted);
        List<MealTo> expectedMealTos = prepareDataMealTos(SecurityUtil.caloriesMin(), SecurityUtil.caloriesMax(),
                101, MEAL_2, MEAL_3, MEAL_4, MEAL_5);
        Assert.assertEquals(expectedMealTos, controller.getAll());
    }

    @Test
    public void create() {
        Meal created = prepareDataMeal(MEAL_6, 0, SecurityUtil.getUser());
        Meal expected = prepareDataMeal(MEAL_6, 105, SecurityUtil.getUser());
        Meal returnMeal = controller.create(created);

        Assert.assertEquals(expected, returnMeal);
        List<MealTo> expectedMealTos = prepareDataMealTos(SecurityUtil.caloriesMin(), SecurityUtil.caloriesMax(),
                100, MEAL_1, MEAL_2, MEAL_3, MEAL_4, MEAL_5, MEAL_6);
        Assert.assertEquals(expectedMealTos, controller.getAll());
    }

    @Test
    public void update() {
        int id = 100;
        Meal updated = prepareDataMeal(MEAL_1, id, SecurityUtil.getUser());
        updated.setDescription(updated.getDescription() + " updated");

        Meal expected = MealsUtil.clone(updated);
        controller.update(updated, updated.getId());
        Assert.assertEquals(MealsUtil.getTo(expected), controller.get(id));
    }

    @Test
    public void exceptionUpdateNotFoundMeal() {
        Meal updated = prepareDataMeal(MEAL_1, 9999, SecurityUtil.getUser());
        updated.setDescription(updated.getDescription() + " updated");
        Assert.assertThrows(NotFoundException.class, () -> controller.update(updated, updated.getId()));
    }

    @Test
    public void exceptionUpdateUserNotOwn() {
        Meal updated = prepareDataMeal(MEAL_1, 100, SecurityUtil.getUser());
        SecurityUtil.setUser(UserUtil.clone(NEW_USER));
        updated.setDescription(updated.getDescription() + " updated");
        Assert.assertThrows(UserNotOwnException.class, () -> controller.update(updated, updated.getId()));
    }

    @Test
    public void exceptionDeleteNotFoundMeal() {
        Assert.assertThrows(NotFoundException.class, () -> controller.delete(9999));
    }

    @Test
    public void exceptionDeleteUserNotOwn() {
        SecurityUtil.setUser(UserUtil.clone(NEW_USER));
        Assert.assertThrows(NotFoundException.class, () -> controller.delete(9999));
    }

    @Test
    public void exceptionGetNotFoundMeal() {
        Assert.assertThrows(NotFoundException.class, () -> controller.get(9999));
    }

    @Test
    public void exceptionGetUserNotOwn() {
        SecurityUtil.setUser(UserUtil.clone(NEW_USER));
        Assert.assertThrows(NotFoundException.class, () -> controller.get(9999));
    }
}