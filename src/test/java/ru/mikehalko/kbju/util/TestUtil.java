package ru.mikehalko.kbju.util;

import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.user.UserCredential;
import ru.mikehalko.kbju.to.MealTo;
import ru.mikehalko.kbju.util.model.MealsUtil;
import ru.mikehalko.kbju.util.model.UserCredentialUtil;
import ru.mikehalko.kbju.util.model.UserUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TestUtil {
// meals
    public static List<MealTo> prepareDataMealTos(int caloriesMinExcess, int caloriesMaxExcess, int startId, Meal... meals) {
        List<Meal> expectedMeals = prepareDataMeals(startId, meals);
        return MealsUtil.getTos(expectedMeals, caloriesMinExcess, caloriesMaxExcess);
    }

    public static List<Meal> prepareDataMeals(int startId, Meal... meals) {
        Meal[] expectedMeals = MealsUtil.clone(meals);
        int idCounter = startId;
        for (Meal meal : expectedMeals)
            meal.setId(idCounter++);
        return List.of(expectedMeals);
    }

    public static Meal prepareDataMeal(Meal meal, int id, User owner) {
        Meal result = MealsUtil.clone(meal);
        result.setId(id);
        result.setUser(owner);

        return result;
    }


// users
    public static List<User> prepareDataUsers(int startId, User... users) {
        User[] expectedUsers = UserUtil.clone(users);
        int idCounter = startId;
        for (User user : expectedUsers)
            user.setId(idCounter++);
        return List.of(expectedUsers);
    }

    public static User prepareDataUser(User user, int id, List<Meal> meals) {
        User result = UserUtil.clone(user);
        result.setId(id);
        result.setMeals(meals);

        return result;
    }

// User Credential
    public static List<UserCredential> prepareDataUserCredentials(int startUserId, UserCredential... credentials) {
        UserCredential[] expectedCredentials = UserCredentialUtil.clone(credentials);
        int idCounter = startUserId;
        for (UserCredential credential : expectedCredentials) {
            User user = new User(); // !
            user.setId(idCounter++);
            credential.setUser(user);
        }
        return List.of(expectedCredentials);
    }

    public static UserCredential prepareDataUserCredential(UserCredential credential) {
        return UserCredentialUtil.clone(credential);
    }

    public static UserCredential prepareDataUserCredential(User user, UserCredential credential) {
        UserCredential result = UserCredentialUtil.clone(credential);
        result.setUser(user);

        return result;
    }


// other
    private static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public static void executeSQLFromFile(Connection connection, String sql_file_path) throws SQLException, IOException {
        Statement statement = connection.createStatement();
        String PrepareDBSQL = readFile(sql_file_path);
        statement.execute(PrepareDBSQL);
        statement.close();
    }
}
