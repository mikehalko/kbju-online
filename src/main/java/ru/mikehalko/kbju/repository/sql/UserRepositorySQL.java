package ru.mikehalko.kbju.repository.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.repository.UserRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ru.mikehalko.kbju.util.sql.ConnectionDataBase;

import static ru.mikehalko.kbju.util.sql.ConstantProperties.*;
import static ru.mikehalko.kbju.util.sql.ResultSetSQLParser.parseUser;
import static ru.mikehalko.kbju.util.sql.ResultSetSQLParser.parseUsersInList;


public class UserRepositorySQL implements UserRepository, Connectable {
    private final Logger log = LoggerFactory.getLogger(UserRepositorySQL.class);
    private ConnectionDataBase connection;

    private static UserRepositorySQL instance;

    private UserRepositorySQL() {

    }

    public static synchronized UserRepositorySQL getInstance() {
        if (instance == null) {
            instance = new UserRepositorySQL();
        }
        return instance;
    }

    public ConnectionDataBase getConnection() {
        return connection;
    }

    public void setConnection(ConnectionDataBase connection) {
        this.connection = connection;
    }


    @Override
    public User save(User user) {
        if (!user.isNew()) {
            return update(user);
        }
        log.debug("save user={}", user);

        String userSaveSQL = String.format(
                "INSERT INTO \"%1$s\" (%2$s, %3$s, %4$s) " +
                        "VALUES ('%5$s', %6$d, %7$d) " +
                        "RETURNING %8$s;",
                USER_TABLE,
                USER_NAME, USER_CALORIES_MIN_PER_DAY, USER_CALORIES_MAX_PER_DAY,
                user.getName(), user.getCaloriesMin(), user.getCaloriesMax(),
                USER_ID
        );
        log.debug("SQL = {}", userSaveSQL);

        try (ResultSet rs = connection.executeQuery(userSaveSQL)) {
            rs.next();
            user.setId(rs.getInt(USER_ID));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public User update(User user) {
        log.debug("update meal={}", user);
        String userUpdateSQL = String.format(
                "UPDATE \"%1$s\" SET " +
                        "%2$s = '%3$s', %4$s = %5$d, %6$s = %7$d " +
                        "WHERE %8$s = %9$d RETURNING %10$s;",
                USER_TABLE, // 1
                USER_NAME, user.getName(), // 2, 3
                USER_CALORIES_MIN_PER_DAY, user.getCaloriesMin(), // 4, 5
                USER_CALORIES_MAX_PER_DAY, user.getCaloriesMax(), // 6, 7
                USER_ID, user.getId(), USER_ID // 8, 9, 10
        );
        log.debug("SQL = {}", userUpdateSQL);

        try (ResultSet rs = connection.executeQuery(userUpdateSQL);) {
            rs.next();
            int returnedId = rs.getInt(USER_ID);
            if (user.getId() != returnedId) {
                log.error("user with id = {} not updated. Query return user_id = {}", user.getId(), returnedId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public boolean delete(int id) {
        log.debug("delete id={}", id);
        String userDeleteSQL = String.format(
                "DELETE FROM \"%1$s\" WHERE %2$s = %3$d RETURNING %4$s;",
                USER_TABLE, USER_ID, id, USER_ID
        );
        log.debug("SQL = {}", userDeleteSQL);
        boolean response = false;
        try (ResultSet rs = connection.executeQuery(userDeleteSQL)) {
            rs.next();
            response = rs.getInt(USER_ID) != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    // meal without user
    @Override
    public User get(int id) {

        log.debug("get by id={}", id);

        String userSQL = String.format(
                "SELECT * FROM \"%1$s\" WHERE %2$s = %3$d;",
                USER_TABLE, USER_ID, id
        );
        log.debug("SQL = {}", userSQL);
        User user = null;
        try (ResultSet rs = connection.executeQuery(userSQL);) {
            if (!rs.next()) {
                log.debug("get result is empty");
                return null;
            }
            user = parseUser(rs);
            user.setMeals(null); // !
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        log.debug("selectAll");
        String userSQL = String.format(
                "SELECT * FROM \"%1$s\";",
                USER_TABLE
        );

        List<User> users = new ArrayList<>();
        try (ResultSet rs = connection.executeQuery(userSQL);) {
            parseUsersInList(rs, users);
            if (users.isEmpty()) log.debug("result users is empty");
        } catch (SQLException e) {
            throw new RuntimeException("get all sql exception", e);
        }

        log.debug("selectAll result = {}", users);

        return users;
    }
}
