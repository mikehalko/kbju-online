package ru.mikehalko.kbju.util.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ConstantProperties {

    public static String DB_CLASS_DRIVER;
    public static String DB_URL;
    public static String DB_USER;
    public static String DB_PASS;

    public static String USER_TABLE;
    public static String MEAL_TABLE;
    public static String USER_CREDENTIAL_TABLE;

    public static String USER_ID;
    public static String USER_NAME;
    public static String USER_CALORIES_MIN_PER_DAY;
    public static String USER_CALORIES_MAX_PER_DAY;

    public static String MEAL_ID;
    public static String MEAL_DATETIME;
    public static String MEAL_MASS;
    public static String MEAL_DESCRIPTION;
    public static String MEAL_PROTEINS;
    public static String MEAL_FATS;
    public static String MEAL_CARBOHYDRATES;
    public static String MEAL_CALORIES;

    public static String USER_CREDENTIAL_USER_ID;
    public static String USER_CREDENTIAL_LOGIN;
    public static String USER_CREDENTIAL_PASSWORD;

    private static final Logger log = LoggerFactory.getLogger(ConstantProperties.class);

    private ConstantProperties() {}

    public static Properties properties(String path_DB_properties) throws IOException {
        log.debug("properties path = {}", path_DB_properties);
        Properties properties = null;
        try (FileInputStream stream = new FileInputStream(path_DB_properties);) {
            properties = new Properties();
            properties.load(stream);
        } catch (IOException e) {
            log.error("property file read error", e);
            throw e;
        }

        return properties;
    }

    public static void initDBAll(String path_DB_properties) throws IOException {
        log.debug("init path DB properties path = {}", path_DB_properties);
        Properties properties = properties(path_DB_properties);

        initDBAll(properties);
    }

    public static void initDBAll(Properties properties) {
        initDBCredentials(properties);
        initDBMealAndUserTables(properties);
        initDBMealAndUserColumns(properties);
        initDBCredentialColumns(properties);
    }

    public static void initDBCredentials(Properties properties) {
        DB_CLASS_DRIVER = properties.getProperty("DB.DB_CLASS_DRIVER");
        DB_URL = properties.getProperty("DB.DB_URL");
        DB_USER = properties.getProperty("DB.USER");
        DB_PASS = properties.getProperty("DB.PASS");
        checkNull(DB_CLASS_DRIVER, DB_URL, DB_USER, DB_PASS);
    }

    public static void initDBMealAndUserTables(Properties properties) {
        USER_TABLE = properties.getProperty("USER.TABLE_NAME");
        MEAL_TABLE = properties.getProperty("MEAL.TABLE_NAME");
        USER_CREDENTIAL_TABLE = properties.getProperty("USER_CREDENTIAL.TABLE_NAME");
        checkNull(USER_TABLE, MEAL_TABLE, USER_CREDENTIAL_TABLE);

    }

    public static void initDBMealAndUserColumns(Properties properties) {
        MEAL_ID = properties.getProperty("MEAL.col.id");
        MEAL_DATETIME = properties.getProperty("MEAL.col.dateTime");
        MEAL_MASS = properties.getProperty("MEAL.col.mass");
        MEAL_DESCRIPTION = properties.getProperty("MEAL.col.description");
        MEAL_PROTEINS = properties.getProperty("MEAL.col.proteins");
        MEAL_FATS = properties.getProperty("MEAL.col.fats");
        MEAL_CARBOHYDRATES = properties.getProperty("MEAL.col.carbohydrates");
        MEAL_CALORIES = properties.getProperty("MEAL.col.calories");
        USER_CALORIES_MIN_PER_DAY = properties.getProperty("USER.col.caloriesMin");
        USER_CALORIES_MAX_PER_DAY = properties.getProperty("USER.col.caloriesMax");
        USER_ID = properties.getProperty("USER.col.id");
        USER_NAME = properties.getProperty("USER.col.name");
        checkNull(MEAL_ID, MEAL_DATETIME, MEAL_MASS, MEAL_DESCRIPTION, MEAL_PROTEINS, MEAL_FATS, MEAL_CARBOHYDRATES
                , MEAL_CALORIES, USER_CALORIES_MIN_PER_DAY, USER_CALORIES_MAX_PER_DAY, USER_ID, USER_NAME);
    }

    public static void initDBCredentialColumns(Properties properties) {
        USER_CREDENTIAL_USER_ID = properties.getProperty("USER_CREDENTIAL.col.user_id");
        USER_CREDENTIAL_LOGIN = properties.getProperty("USER_CREDENTIAL.col.name");
        USER_CREDENTIAL_PASSWORD = properties.getProperty("USER_CREDENTIAL.col.password");
        checkNull(USER_CREDENTIAL_USER_ID, USER_CREDENTIAL_LOGIN, USER_CREDENTIAL_PASSWORD);
    }

    private static void checkNull(String tag, String... constants) {
        int counter = 0;
        for (String constant : constants) {
            counter++;
            if (Objects.isNull(constant))
                throw new RuntimeException(String.format("[%s]: constant N%d is null",tag, counter)); // TODO свое exception
        }
    }
}
