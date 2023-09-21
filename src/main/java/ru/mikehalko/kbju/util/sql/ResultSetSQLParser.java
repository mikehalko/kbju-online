package ru.mikehalko.kbju.util.sql;

import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.meal.Nutritionally;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.mikehalko.kbju.util.sql.ConstantProperties.*;
import static ru.mikehalko.kbju.util.sql.ConstantProperties.MEAL_CALORIES;

public class ResultSetSQLParser {
    public static User parseUser(ResultSet rs) throws SQLException {
        return parseUser(rs, null);
    }

    public static User parseUser(ResultSet rs, List<Meal> setMeals) throws SQLException {
        int id = 0;
        String name = null;
        int caloriesMax = 0;
        int caloriesMin = 0;
        User user = null;

        id = rs.getInt(USER_ID);
        name = rs.getString(USER_NAME);
        caloriesMin = rs.getInt(USER_CALORIES_MIN_PER_DAY);
        caloriesMax = rs.getInt(USER_CALORIES_MAX_PER_DAY);
        user = new User(id, name, caloriesMin, caloriesMax, setMeals);

        return user;
    }

    public static List<User> parseUsers(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>(); // TODO или принять в аргументах?
        while (rs.next()) {
            users.add(parseUser(rs));
        }

        return users;
    }

    public static Meal parseMeal(ResultSet rs) throws SQLException {
        return parseMeal(rs, null);
    }

    public static Meal parseMeal(ResultSet rs, User setUser) throws SQLException {
        int id = 0;
        LocalDateTime dateTime = null;
        int mass = 0;
        String description = null;
        Nutritionally nut = null;

        id = rs.getInt(MEAL_ID);
        dateTime = rs.getTimestamp(MEAL_DATETIME).toLocalDateTime();
        mass = rs.getInt(MEAL_MASS);
        description = rs.getString(MEAL_DESCRIPTION);

        nut = parseNut(rs);
        return new Meal(id, setUser, dateTime, mass, description, nut);
    }

    public static List<Meal> parseMeals(ResultSet rs) throws SQLException {
        return parseMeals(rs, null);
    }
    public static List<Meal> parseMeals(ResultSet rs, User setUser) throws SQLException {
        List<Meal> meals = new ArrayList<>(); // TODO или принять в аргументах?
        while (rs.next()) {
            meals.add(parseMeal(rs, setUser));
        }

        return meals;
    }

    public static Nutritionally parseNut(ResultSet rs) throws SQLException {
        int proteins = 0;
        int fats = 0;
        int carbohydrates = 0;
        int calories = 0;

        proteins = rs.getInt(MEAL_PROTEINS);
        fats = rs.getInt(MEAL_FATS);
        carbohydrates = rs.getInt(MEAL_CARBOHYDRATES);
        calories = rs.getInt(MEAL_CALORIES);

        return new Nutritionally(proteins, fats, carbohydrates, calories);
    }
}
