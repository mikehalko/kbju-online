package ru.mikehalko.kbju.repository.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.repository.MealRepository;
import ru.mikehalko.kbju.util.sql.ConnectionDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.mikehalko.kbju.util.sql.ConstantProperties.*;
import static ru.mikehalko.kbju.util.sql.ResultSetSQLParser.*;

public class MealRepositorySQL implements MealRepository, Connectable {

    private final Logger log = LoggerFactory.getLogger(MealRepositorySQL.class);
    private ConnectionDataBase connection;

    private static MealRepositorySQL instance;

    private MealRepositorySQL() {}

    public static synchronized MealRepositorySQL getInstance() {
        if (instance == null) {
            instance = new MealRepositorySQL();
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
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew()) {
            return update(meal, userId);
        }
        log.debug("save meal={}, by user_id={}", meal, userId);

        String mealSaveSQL = String.format(
                "INSERT INTO \"%1$s\" (%2$s, %3$s, %4$s, %5$s, %6$s, %7$s, %8$s, %9$s) " +
                        "VALUES (%10$d, '%11$s %12$s', '%13$s', %14$s, %15$s, %16$s, %17$s, %18$s) " +
                        "RETURNING %19$s;",
                MEAL_TABLE,
                USER_ID, MEAL_DATETIME, MEAL_DESCRIPTION, MEAL_MASS, MEAL_PROTEINS, MEAL_FATS, MEAL_CARBOHYDRATES, MEAL_CALORIES,
                userId, meal.getDate(), meal.getTime(), meal.getDescription(), meal.getMass(),
                meal.getNutritionally().getProteins(), meal.getNutritionally().getFats(),
                meal.getNutritionally().getCarbohydrates(), meal.getNutritionally().getCalories(),
                MEAL_ID
        );
        log.debug("SQL = {}", mealSaveSQL);

        try (ResultSet rs = connection.executeQuery(mealSaveSQL)) {
            rs.next();
            meal.setId(rs.getInt(MEAL_ID)); // чтобы не делать второй запрос с get
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return meal;
    }

    public Meal update(Meal meal, int userId) {
        log.debug("update meal={}, by user_id={}", meal, userId);
        String mealUpdateSQL = String.format(
                "UPDATE \"%1$s\" SET " +
                        "%2$s = '%3$s %4$s', %5$s = '%6$s', %7$s = %8$s, %9$s = %10$s, " +
                        "%11$s = %12$s, %13$s = %14$s, %15$s = %16$s " +
                        "WHERE %17$s = %18$s AND %19$s = %20$s RETURNING %21$s;",
                MEAL_TABLE, // 1
                MEAL_DATETIME, meal.getDate(), meal.getTime(), // 2, 3, 4
                MEAL_DESCRIPTION, meal.getDescription(), // 5, 6
                MEAL_MASS, meal.getMass(), // 7, 8
                MEAL_PROTEINS, meal.getNutritionally().getProteins(), // 9, 10
                MEAL_FATS, meal.getNutritionally().getFats(), // 11, 12
                MEAL_CARBOHYDRATES, meal.getNutritionally().getCarbohydrates(), // 13, 14
                MEAL_CALORIES, meal.getNutritionally().getCalories(), // 15, 16
                MEAL_ID, meal.getId(), // 17, 18
                USER_ID, userId, MEAL_ID // 19, 20, 21
        );
        log.debug("SQL = {}", mealUpdateSQL);

        try (ResultSet rs = connection.executeQuery(mealUpdateSQL);) {
            rs.next();
            int returnedId = rs.getInt(MEAL_ID);
            if (meal.getId() != returnedId) {
                log.error("meal with id = {} not updated. Query return meal_id = {}", meal.getId(), returnedId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.debug("delete by id={}, user_id = {}", id, userId);
        String mealDeleteSQL = String.format(
                "DELETE FROM \"%1$s\" WHERE %2$s = %3$d AND %4$s = %5$d RETURNING %6$s;",
                MEAL_TABLE, MEAL_ID, id, USER_ID, userId, MEAL_ID
        );
        log.debug("SQL = {}", mealDeleteSQL);
        boolean response = false;
        try (ResultSet rs = connection.executeQuery(mealDeleteSQL)) {
            rs.next();
            response = rs.getInt(MEAL_ID) != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    @Override
    public Meal get(int id, int userId) {
        log.debug("get by id={}, user_id={}", id, userId);

        String mealSQL = String.format(
                "SELECT * FROM \"%1$s\" WHERE %2$s = %3$d AND %4$s = %5$d;",
                MEAL_TABLE, MEAL_ID, id, USER_ID, userId
        );
        log.debug("SQL = {}", mealSQL);
        Meal meal = null;
        try (ResultSet rs = connection.executeQuery(mealSQL);) {
            if (!rs.next()) {
                log.debug("get result is empty");
                return null;
            }
            meal = parseMeal(rs);
            meal.setUser(null); // !
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.debug("getAll by {}", userId);
        String mealsSQL = String.format(
                "SELECT * FROM \"%1$s\" WHERE %2$s = %3$d;",
                MEAL_TABLE, USER_ID, userId
        );

        List<Meal> meals = new ArrayList<>();
        try (ResultSet rs = connection.executeQuery(mealsSQL);) {
            parseMealsInList(rs, meals);
            if (meals.isEmpty()) log.debug("result meals is empty");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        log.debug("getAll result = {}", meals);

        return meals;
    }
}