package ru.mikehalko.kbju.test;

import ru.mikehalko.kbju.controller.MealController;
import ru.mikehalko.kbju.model.Meal;
import ru.mikehalko.kbju.repository.MealRepository;
import ru.mikehalko.kbju.repository.fake.FakeMealRepository;
import ru.mikehalko.kbju.service.MealService;
import ru.mikehalko.kbju.to.MealTo;
import ru.mikehalko.kbju.util.SecurityUtil;
import ru.mikehalko.kbju.util.fake.FakeAuthUser;
import ru.mikehalko.kbju.util.fake.FakeManager;
import ru.mikehalko.kbju.util.fake.FakeBuilder;

import java.time.LocalDateTime;
import java.util.List;

public class Test {
    private static final FakeManager fakeManage;
    private static final Meal MEAL_1;
    private static final int ID = 101;

    private static final int DAYS = 3;
    private static final int MEALS_PER_DAY = 3;

    static {
        FakeBuilder fakeBuilder = new FakeBuilder();
        fakeManage = new FakeManager(new FakeMealRepository(fakeBuilder));
        fakeManage.setNumberOfDays(DAYS);
        fakeManage.setMealsPerDay(MEALS_PER_DAY);
        
        MEAL_1 = fakeBuilder.getMeal(777, FakeAuthUser.user(), LocalDateTime.now(), 
                600, "meal 1", fakeBuilder.getNutritionally(100, 200, 300));
    }

    static MealRepository repository = fakeManage.getRepository();
    static MealService service = new MealService(repository);
    static MealController controller = new MealController(service);

    public static void main(String[] args) {
        // визуальный тест
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

        Meal meal = fakeManage.getFakeBuilder().copyMeal(id, MEAL_1);
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
        if (reset) fakeManage.resetRepository();
        print("before:");
        printContent();
    }

    private static void end() {
        print("==================== END ====================");
    }


    private static void printContent() {
        print("------------------ content ------------------");
        print(fakeManage.getTextContentRepository());
        print("---------------- content-end ----------------");
        print("---------------------------------------------");
    }

    private static void print(Object o) {
        System.out.println(o);
    }
}
