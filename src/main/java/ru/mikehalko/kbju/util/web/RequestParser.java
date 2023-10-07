package ru.mikehalko.kbju.util.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.user.UserCredential;
import ru.mikehalko.kbju.util.web.validation.MealValidator;
import ru.mikehalko.kbju.util.web.validation.UserCredentialValidator;
import ru.mikehalko.kbju.util.web.validation.UserValidator;
import ru.mikehalko.kbju.util.web.validation.Validation;
import ru.mikehalko.kbju.util.web.validation.exception.ParamIsEmptyException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

import static ru.mikehalko.kbju.util.model.MealsUtil.createMeal;
import static ru.mikehalko.kbju.util.model.UserUtil.createUser;
import static ru.mikehalko.kbju.util.model.UserCredentialUtil.createCredential;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.attribute.MealField;
import ru.mikehalko.kbju.web.constant.attribute.UserField;
import ru.mikehalko.kbju.web.constant.attribute.UserCredentialField;


// TODO тесты
public class RequestParser {
    private static Logger log = LoggerFactory.getLogger(RequestParser.class);

    public static int parseInt(HttpServletRequest request, Constant param) throws ParamIsEmptyException, NullPointerException {
        return parseInt(request, param, null);
    }

    public static int parseInt(HttpServletRequest request, Constant param, Validation validation) throws NullPointerException {
        try {
            String result = Objects.requireNonNull(request.getParameter(param.value()));
            checkEmpty(result, validation, param);
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            if (validation != null) {
                validation.catchEx(param, e);
                return 0;
            } else {
                throw e;
            }
        }
    }



    public static String parseString(HttpServletRequest request, Constant param) throws ParamIsEmptyException, NullPointerException  {
        return parseString(request, param, null);
    }

    public static String parseString(HttpServletRequest request, Constant param, Validation validation) throws ParamIsEmptyException, NullPointerException  {
        String result = Objects.requireNonNull(request.getParameter(param.value()));
        checkEmpty(result, validation, param);
        return result;
    }


    public static LocalDateTime localDateTime(HttpServletRequest request, MealField param) throws ParamIsEmptyException, NullPointerException {
        return localDateTime(request, param, null);
    }

    public static LocalDateTime localDateTime(HttpServletRequest request, MealField param, Validation validation) throws ParamIsEmptyException, NullPointerException {
        try{
            String dateTime = Objects.requireNonNull(request.getParameter(param.value()));
            checkEmpty(dateTime, validation, param);
            return LocalDateTime.parse(dateTime);
        } catch (NumberFormatException e) {
            if (validation != null) {
                validation.catchEx(param, e);
                return LocalDateTime.MIN;
            } else {
                throw e;
            }
        }
    }


    public static Meal meal(HttpServletRequest request, User user) {
        int id = parseInt(request, MealField.PARAM_ID);
        LocalDateTime dateTime = localDateTime(request, MealField.PARAM_DATE_TIME);
        String description = parseString(request, MealField.PARAM_DESCRIPTION);
        int mass = parseInt(request, MealField.PARAM_MASS);
        int proteins = parseInt(request, MealField.PARAM_PROTEINS);
        int fats = parseInt(request, MealField.PARAM_FATS);
        int carbohydrates = parseInt(request, MealField.PARAM_CARBOHYDRATES);
        int calories = parseInt(request, MealField.PARAM_CALORIES);

        return createMeal(id, user, dateTime, mass, description, proteins, fats, carbohydrates, calories);
    }

    public static Meal meal(HttpServletRequest request, User user, @NotNull MealValidator validation) {
        int id = parseInt(request, MealField.PARAM_ID, validation);
        LocalDateTime dateTime = localDateTime(request, MealField.PARAM_DATE_TIME, validation);
        String description = parseString(request, MealField.PARAM_DESCRIPTION, validation);
        int mass = parseInt(request, MealField.PARAM_MASS, validation);
        int proteins = parseInt(request, MealField.PARAM_PROTEINS, validation);
        int fats = parseInt(request, MealField.PARAM_FATS, validation);
        int carbohydrates = parseInt(request, MealField.PARAM_CARBOHYDRATES, validation);
        int calories = parseInt(request, MealField.PARAM_CALORIES, validation);

        if (validation.isNotValid()) {
            log.debug("user parsing with validation has exceptions, return null");
            return null;
        }

        validation.id(id);
        validation.dateTime(dateTime);
        validation.description(description);
        validation.params(
                new MealField[]{MealField.PARAM_MASS, MealField.PARAM_PROTEINS, MealField.PARAM_FATS, MealField.PARAM_CARBOHYDRATES, MealField.PARAM_CALORIES},
                new int[]{mass, proteins, fats, carbohydrates, calories});

        Meal result = createMeal(id, user, dateTime, mass, description, proteins, fats, carbohydrates, calories);
        if (validation.isNotValid()) {
            log.debug("meal parsing with validation has invalid fields; set 0 calories; return INVALID meal");
            result.getNutritionally().setCalories(0);
        } else {
            log.debug("no one invalid fields");
        }

        return result;
    }

    public static User user(HttpServletRequest request, @NotNull UserValidator validation) {
        int id = parseInt(request, UserField.PARAM_USER_ID, validation);
        String name = parseString(request, UserField.PARAM_NAME, validation);
        int caloriesMin = parseInt(request, UserField.PARAM_CALORIES_MIN, validation);
        int caloriesMax = parseInt(request, UserField.PARAM_CALORIES_MAX, validation);

        if (validation.isNotValid()) {
            log.debug("user parsing with validation has exceptions, return null");
            return null;
        }

        validation.id(id);
        validation.name(name);
        validation.calories(caloriesMin, caloriesMax);

        User result = createUser(id, name, caloriesMin, caloriesMax);
        if (validation.isNotValid()) {
            log.debug("user parsing with validation has invalid fields; return INVALID user");
        } else {
            log.debug("no one invalid fields");
        }

        return result;
    }

    public static UserCredential credentialNew(HttpServletRequest request, User owner, @NotNull UserCredentialValidator validation) {
        String login = parseString(request, UserCredentialField.PARAM_LOGIN, validation);
        if (validation.isNotValid()) {
            log.debug("\"{}\" parsing (with validation) has exceptions", UserCredentialField.PARAM_LOGIN);
        }
        UserCredential result = credentialPasswordWithRepeat(request, owner, validation);
        if (result == null) return null;
        result.setLogin(login);
        validation.login(login);

        if (validation.isNotValid()) {
            log.debug("credential parsing with validation has invalid fields; return INVALID credential");
        } else {
            log.debug("no one invalid fields");
        }

        return result;
    }

    public static UserCredential passwordWithRepeat(HttpServletRequest request, User owner, @NotNull UserCredentialValidator validation) {
        UserCredential result = credentialPasswordWithRepeat(request, owner, validation);
        if (validation.isNotValid()) {
            log.debug("credential parsing with validation has invalid fields; return INVALID credential");
        } else {
            log.debug("no one invalid fields");
        }
        return result;
    }

    private static UserCredential credentialPasswordWithRepeat(HttpServletRequest request, User owner, @NotNull UserCredentialValidator validation) {
        String password = parseString(request, UserCredentialField.PARAM_PASSWORD_NEW, validation);
        String passwordRepeat = parseString(request, UserCredentialField.PARAM_PASSWORD_REPEAT, validation);

        if (validation.isNotValid()) {
            log.debug("\"{}\" \"{}\" parsing (with validation) has exceptions, return null",
                    UserCredentialField.PARAM_PASSWORD_NEW, UserCredentialField.PARAM_PASSWORD_REPEAT);
            return null;
        }
        validation.password(password);
        validation.passwordRepeatEquals(password, passwordRepeat);
        return createCredential(owner, null, password);
    }


    private static void checkEmpty(String str) throws ParamIsEmptyException {
        if (str.isEmpty()) throw new ParamIsEmptyException();
    }

    private static void checkEmpty(String str, Validation validation, Constant param) throws ParamIsEmptyException {
        try {
            checkEmpty(str);
        } catch (ParamIsEmptyException e) {
            if (validation != null) validation.catchEx(param, e);
            else throw e;
        }
    }
}
