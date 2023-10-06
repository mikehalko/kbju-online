package ru.mikehalko.kbju.data;

import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.meal.Nutritionally;
import ru.mikehalko.kbju.model.user.UserCredential;

import java.time.LocalDateTime;

public class TestData {
    public static final User NEW_USER = new User(0, "NEW_USER", 1450, 2250);

    public static final User USER_1 = new User(1, "USER_1", 1000, 1800);
    public static final User USER_2 = new User(2, "USER_2", 1500, 2500);
    public static final User USER_3 = new User(3, "USER_3", 1900, 2800);
    public static final User USER_4 = new User(4, "USER_4", 1200, 2200);
    public static final User USER_5 = new User(5, "USER_5", 1300, 2300);

    public static final UserCredential NEW_CREDENTIAL = new UserCredential(null, NEW_USER.getName(), "new");

    public static final UserCredential CREDENTIAL_1 = new UserCredential(USER_1, USER_1.getName(), "1");
    public static final UserCredential CREDENTIAL_2 = new UserCredential(USER_2, USER_2.getName(), "2");
    public static final UserCredential CREDENTIAL_3 = new UserCredential(USER_3, USER_3.getName(), "3");
    public static final UserCredential CREDENTIAL_4 = new UserCredential(USER_4, USER_4.getName(), "4");
    public static final UserCredential CREDENTIAL_5 = new UserCredential(USER_5, USER_5.getName(), "5");

    public static final Meal NEW_MEAL = new Meal(0, null, LocalDateTime.parse("2023-09-04T09:23:54"), 100, "test_0", new Nutritionally(174, 28, 4, 54));

    public static final Meal MEAL_1 = new Meal(1, null, LocalDateTime.parse("2023-09-02T09:23:54"), 174, "test_1_day-1", new Nutritionally(28, 44, 0, 512));
    public static final Meal MEAL_2 = new Meal(2, null, LocalDateTime.parse("2023-09-02T13:10:20"), 297, "test_2_day-1", new Nutritionally(15, 21, 50, 472));
    public static final Meal MEAL_3 = new Meal(3, null, LocalDateTime.parse("2023-09-02T20:43:42"), 369, "test_3_day-1", new Nutritionally(11, 15, 66, 417));

    public static final Meal MEAL_4 = new Meal(4, null, LocalDateTime.parse("2023-09-03T09:15:20"), 318, "test_4_day-2", new Nutritionally(35, 32, 51, 445));
    public static final Meal MEAL_5 = new Meal(5, null, LocalDateTime.parse("2023-09-03T14:00:00"), 848, "test_5_day-2", new Nutritionally(25, 17, 34, 288));
    public static final Meal MEAL_6 = new Meal(6, null, LocalDateTime.parse("2023-09-03T19:50:32"), 218, "test_6_day-2", new Nutritionally(22, 24, 7, 241));

    public static final Meal MEAL_7 = new Meal(7, null, LocalDateTime.parse("2023-09-03T09:15:20"), 318, "test_7_day-3", new Nutritionally(35, 32, 51, 945));
    public static final Meal MEAL_8 = new Meal(8, null, LocalDateTime.parse("2023-09-03T14:00:00"), 848, "test_8_day-3", new Nutritionally(25, 17, 34, 588));
    public static final Meal MEAL_9 = new Meal(9, null, LocalDateTime.parse("2023-09-03T19:50:32"), 218, "test_9_day-3", new Nutritionally(22, 24, 7, 641));


    public static final String POSTGRES_PROPERTIES_PATH = "src/main/resources/db/postgres.properties";
    public static final String PREPARE_DB_SQL_PATH = "src/test/java/ru/mikehalko/kbju/resources/sql/prepareDB.sql";
    public static final String CLEAN_AND_INIT_SQL_PATH = "src/test/java/ru/mikehalko/kbju/resources/sql/cleanAndInit.sql";
    public static final String DROP_TEST_TABLES_SQL_PATH = "src/test/java/ru/mikehalko/kbju/resources/sql/dropTestTables.sql";

}