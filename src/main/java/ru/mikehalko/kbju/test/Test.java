package ru.mikehalko.kbju.test;

import ru.mikehalko.kbju.controller.MealController;
import ru.mikehalko.kbju.model.Meal;
import ru.mikehalko.kbju.service.MealService;
import ru.mikehalko.kbju.to.MealTo;
import ru.mikehalko.kbju.util.SecurityUtil;
import ru.mikehalko.kbju.util.fake.FakeManager;

import java.time.LocalDateTime;
import java.util.List;

public class Test {
    private static final FakeManager fm;
    private static final Meal MEAL_1;
    private static final int ID = 101;

    private static final int DAYS = 3;
    private static final int MEALS_PER_DAY = 3;

    static {
        fm = new FakeManager();
        SecurityUtil.setUser(fm.getFakeAuthUser());
        fm.setUserOwnerMeals(SecurityUtil.getUser());
        fm.resetRepository();
        fm.setNumberOfDays(DAYS);
        fm.setMealsPerDay(MEALS_PER_DAY);

        // meal_id == 0 -> meal is new
        MEAL_1 = fm.fakeBuilder().meal(0, SecurityUtil.getUser(), LocalDateTime.now(),
                600, "meal 1", fm.fakeBuilder().nutritionally(100, 200, 300));
    }

    static MealController controller = new MealController(new MealService(fm.getRepository()));

    public static void main(String[] args) {
        // простейший грубый визуальный тест
        get(true);

        create(true);

        getAll(true);

        update(true);

        delete(true);
    }

    public static void get(boolean reset) {
        start("GET", reset);
        int id = ID;

        MealTo someMeal = controller.get(id);
        print("get meal by id \"" + id + "\": \n" + someMeal);

        printContent();
        end();
    }

    public static void getAll(boolean reset) {
        start("GET-ALL", reset);

        List<MealTo> all = controller.getAll();
        print("get all meals: \n");
        all.forEach(System.out::println);

        printContent();
        end();
    }

    public static void create(boolean reset)    {
        start("CREATE", reset);
        controller.create(MEAL_1);
        print("save " + MEAL_1 + ": \n");
        printContent();
        end();
    }

    public static void update(boolean reset) {
        int id = ID;
        start("UPDATE", reset);
        print("update id="+id);

        Meal meal = fm.fakeBuilder().copyMeal(id, MEAL_1);
        print("with " + meal);
        controller.update(meal, id);
        print("after:");
        printContent();
        end();
    }

    public static void delete(boolean reset) {
        int id = ID;
        start("DELETE", reset);
        print("delete id="+id);
        print("by user auth id="+ SecurityUtil.authId());
        controller.delete(id);
        print("after:");
        printContent();
        end();
    }

    private static void start(String message, boolean reset) {
        print("\n\n=================== START ===================");
        print(message);
        if (reset) fm.resetRepository();
        print("before:");
        printContent();
    }

    private static void end() {
        print("==================== END ====================");
    }


    private static void printContent() {
        print("------------------ content ------------------");
        print(fm.getContentRepositoryInText());
        print("---------------- content-end ----------------");
        print("---------------------------------------------");
    }

    private static void print(Object o) {
        System.out.println(o);
    }
}
