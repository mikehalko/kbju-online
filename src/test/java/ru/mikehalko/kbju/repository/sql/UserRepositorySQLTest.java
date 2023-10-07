package ru.mikehalko.kbju.repository.sql;

import org.junit.*;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.util.sql.ConnectionDataBase;
import ru.mikehalko.kbju.util.sql.ConstantProperties;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static ru.mikehalko.kbju.data.TestData.*;
import static ru.mikehalko.kbju.util.TestUtil.*;

public class UserRepositorySQLTest {

    private static ConnectionDataBase connection;
    private static UserRepositorySQL repository;

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
        repository = UserRepositorySQL.getInstance();
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
        List<User> expectedUsers = prepareDataUsers(1, USER_1, USER_2, USER_3, USER_4, USER_5);
        Assert.assertEquals(expectedUsers, repository.getAll());
    }

    @Test
    public void get() {
        User expected = prepareDataUser(USER_1, USER_1.getId(), null);
        User returnUser = repository.get(USER_1.getId());
        Assert.assertEquals(expected, returnUser);
    }

    @Test
    public void save() {
        User created = prepareDataUser(NEW_USER, 0, null);
        User expected = prepareDataUser(NEW_USER, 6, null);
        User returnUser = repository.save(created);
        Assert.assertEquals(expected, returnUser);
        List<User> expectedUsers = prepareDataUsers(1, USER_1, USER_2, USER_3, USER_4, USER_5, expected);
        Assert.assertEquals(expectedUsers, repository.getAll());
    }

    @Test
    public void update() {
        int id = 5;
        User updated = prepareDataUser(NEW_USER, id, null);
        User expected = prepareDataUser(NEW_USER, id, null);
        User returnMeal = repository.update(updated);
        Assert.assertEquals(expected, returnMeal);
        List<User> expectedUsers = prepareDataUsers(1, USER_1, USER_2, USER_3, USER_4, expected);
        Assert.assertEquals(expectedUsers, repository.getAll());
    }

    @Test
    public void delete() {
        Assert.assertTrue(repository.delete(USER_5.getId()));
        List<User> expectedUsers = prepareDataUsers(1, USER_1, USER_2, USER_3, USER_4);
        Assert.assertEquals(expectedUsers, repository.getAll());
    }
}