package ru.mikehalko.kbju.repository.sql;

import org.junit.*;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.user.UserCredential;
import ru.mikehalko.kbju.util.sql.ConnectionDataBase;
import ru.mikehalko.kbju.util.sql.ConstantProperties;

import java.io.IOException;
import java.sql.SQLException;

import static ru.mikehalko.kbju.data.TestData.*;
import static ru.mikehalko.kbju.util.TestUtil.*;

public class UserCredentialRepositorySQLTest {

    private static ConnectionDataBase connection;
    private static UserCredentialRepositorySQL credRepository;
    private static UserRepositorySQL userRepository;

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
        credRepository = UserCredentialRepositorySQL.getInstance();
        credRepository.setConnection(connection);
        userRepository = UserRepositorySQL.getInstance();
        userRepository.setConnection(connection);

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
            executeSQLFromFile(credRepository.getConnection(), DROP_TEST_TABLES_SQL_PATH);
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
            ConnectionDataBase connection = credRepository.getConnection();
            executeSQLFromFile(connection, PREPARE_DB_SQL_PATH);
            executeSQLFromFile(connection, CLEAN_AND_INIT_SQL_PATH);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void find() {
        UserCredential expected = prepareDataUserCredential(CREDENTIAL_1);
        UserCredential find = prepareDataUserCredential(CREDENTIAL_1);
        int returnCredentialUserId = credRepository.find(find);
        Assert.assertEquals(expected.getUserId(), returnCredentialUserId);
    }

    @Test
    public void save() {
        User user = userRepository.save(NEW_USER);

        UserCredential created = prepareDataUserCredential(NEW_CREDENTIAL);
        created.setUser(user);
        boolean isSaved = credRepository.save(created, user.getId());
        Assert.assertTrue(isSaved);

        UserCredential expected = prepareDataUserCredential(user, NEW_CREDENTIAL);
        Assert.assertEquals(expected.getUserId(), credRepository.find(expected));
    }

    @Test
    public void delete() {
        UserCredential deleted = prepareDataUserCredential(CREDENTIAL_5);
        Assert.assertThrows(UnsupportedOperationException.class, () -> credRepository.delete(deleted));
    }
}