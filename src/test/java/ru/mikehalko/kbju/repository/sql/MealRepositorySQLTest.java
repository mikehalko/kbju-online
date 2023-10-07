package ru.mikehalko.kbju.repository.sql;

import org.junit.*;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.util.sql.ConnectionDataBase;
import ru.mikehalko.kbju.util.sql.ConstantProperties;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import static ru.mikehalko.kbju.data.TestData.*;
import static ru.mikehalko.kbju.util.TestUtil.*;

public class MealRepositorySQLTest {

    private static ConnectionDataBase connection;
    private static MealRepositorySQL repository;

    @BeforeClass
    public static void beforeClass() {
        try {
            ConstantProperties.initProperties(POSTGRES_PROPERTIES_PATH);
        connection = ConnectionDataBase.getConnection(
                ConstantProperties.DB_URL, ConstantProperties.DB_USER,
                ConstantProperties.DB_PASS, ConstantProperties.DB_CLASS_DRIVER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        repository = MealRepositorySQL.getInstance();
        repository.setConnection(connection);

        ConstantProperties.USER_ID = "user_test_id";
        ConstantProperties.MEAL_ID = "meal_test_id";
        ConstantProperties.USER_CREDENTIAL_USER_ID = "user_test_id";
        ConstantProperties.MEAL_TABLE = "meal_test";
        ConstantProperties.USER_TABLE = "user_test";
        ConstantProperties.USER_CREDENTIAL_TABLE = "user_credential_test";
    }

    @AfterClass
    public static void afterClass() {
        try {
            executeSQLFromFile(repository.getConnection(), DROP_TEST_TABLES_SQL_PATH);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() throws Exception {
        try {
            ConnectionDataBase connection = repository.getConnection();
            executeSQLFromFile(connection, PREPARE_DB_SQL_PATH);
            executeSQLFromFile(connection, CLEAN_AND_INIT_SQL_PATH);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAll() {
        List<Meal> expectedMeal = prepareDataMeals(1, MEAL_1, MEAL_2, MEAL_3, MEAL_4,
                MEAL_5, MEAL_6, MEAL_7, MEAL_8, MEAL_9);
        Assert.assertEquals(expectedMeal, repository.getAll(USER_1.getId()));
    }

    @Test
    public void get() {
        Meal expected = prepareDataMeal(MEAL_1, MEAL_1.getId(), null);
        Meal returnMeal = repository.get(MEAL_1.getId(), USER_1.getId());
        Assert.assertEquals(expected, returnMeal);
    }

    @Test
    public void save() {
        Meal created = prepareDataMeal(NEW_MEAL, 0, null);
        Meal expected = prepareDataMeal(NEW_MEAL, 10, null);
        Meal returnMeal = repository.save(created, USER_1.getId());
        Assert.assertEquals(expected, returnMeal);
        List<Meal> expectedMeal = prepareDataMeals(1, MEAL_1, MEAL_2, MEAL_3, MEAL_4,
                                                                MEAL_5, MEAL_6, MEAL_7, MEAL_8, MEAL_9, expected);
        Assert.assertEquals(expectedMeal, repository.getAll(USER_1.getId()));
    }

    @Test
    public void update() {
        int id = 9;
        Meal updated = prepareDataMeal(NEW_MEAL, id, null);
        Meal expected = prepareDataMeal(NEW_MEAL, id, null);
        Meal returnMeal = repository.update(updated, USER_1.getId());
        Assert.assertEquals(expected, returnMeal);

        List<Meal> expectedMeals = prepareDataMeals(1, MEAL_1, MEAL_2, MEAL_3, MEAL_4,
                                                                MEAL_5, MEAL_6, MEAL_7, MEAL_8, expected);
        Assert.assertEquals(expectedMeals, repository.getAll(USER_1.getId()));
    }

    @Test
    public void delete() {
        Assert.assertTrue(repository.delete(MEAL_1.getId(), USER_1.getId()));
        List<Meal> expectedMeal = prepareDataMeals(2, MEAL_2, MEAL_3, MEAL_4,
                                                                MEAL_5, MEAL_6, MEAL_7, MEAL_8, MEAL_9);
        Assert.assertEquals(expectedMeal, repository.getAll(USER_1.getId()));
    }
}