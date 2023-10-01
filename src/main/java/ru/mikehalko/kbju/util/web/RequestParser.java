package ru.mikehalko.kbju.util.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.user.UserCredential;
import ru.mikehalko.kbju.util.web.validation.MealValidation;
import ru.mikehalko.kbju.util.web.validation.UserCredentialValidation;
import ru.mikehalko.kbju.util.web.validation.UserValidation;
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
import ru.mikehalko.kbju.web.constant.attribute.MealAttribute;
import ru.mikehalko.kbju.web.constant.attribute.UserAttribute;
import ru.mikehalko.kbju.web.constant.attribute.UserCredentialAttribute;


// TODO тесты
public class RequestParser {
    private static Logger log = LoggerFactory.getLogger(RequestParser.class);

    public static int parseInt(HttpServletRequest request, Constant param) throws NullPointerException {
        return parseInt(request, param, true);
    }

    public static int parseInt(HttpServletRequest request, Constant param, boolean canBeEmpty) throws ParamIsEmptyException, NullPointerException {
        return parseInt(request, param, canBeEmpty, null);
    }

    public static int parseInt(HttpServletRequest request, Constant param, boolean canBeEmpty, Validation validation) throws NullPointerException {
        try {
            String result = Objects.requireNonNull(request.getParameter(param.value()));
            if (!canBeEmpty) {
                checkEmpty(result, validation, param.value());
                return Integer.parseInt(result);
            } else {
                return result.isEmpty() ? 0 : Integer.parseInt(result);
            }
        } catch (NumberFormatException e) {
            if (validation != null) {
                validation.catchEx(param.value(), e);
                return 0;
            } else {
                throw e;
            }
        }
    }

    public static String parseString(HttpServletRequest request, Constant param) throws NullPointerException  {
        return parseString(request, param, true, null);
    }

    public static String parseString(HttpServletRequest request, Constant param, boolean canBeEmpty) throws ParamIsEmptyException, NullPointerException  {
        return parseString(request, param, canBeEmpty, null);
    }

    public static String parseString(HttpServletRequest request, Constant param, boolean canBeEmpty, Validation validation) throws ParamIsEmptyException, NullPointerException  {
        String result = Objects.requireNonNull(request.getParameter(param.value()));
        if (!canBeEmpty) {
            checkEmpty(result, validation, param.value());
        }
        return result;
    }

    public static LocalDateTime parseLocalDateTime(HttpServletRequest request, MealAttribute param) throws ParamIsEmptyException, NullPointerException {
        return parseLocalDateTime(request, param, true, null);
    }

    public static LocalDateTime parseLocalDateTime(HttpServletRequest request, MealAttribute param, boolean canBeEmpty) throws ParamIsEmptyException, NullPointerException {
        return parseLocalDateTime(request, param, canBeEmpty, null);
    }

    public static LocalDateTime parseLocalDateTime(HttpServletRequest request, MealAttribute param, boolean canBeEmpty, Validation validation) throws ParamIsEmptyException, NullPointerException {
        try{
            String dateTime = Objects.requireNonNull(request.getParameter(param.value()));

            if (!canBeEmpty) {
                checkEmpty(dateTime, validation, param.value());
                return LocalDateTime.parse(dateTime);
            } else {
                return dateTime.isEmpty() ? LocalDateTime.MIN : LocalDateTime.parse(dateTime); // not safe
            }
        } catch (NumberFormatException e) {
            if (validation != null) {
                validation.catchEx(param.value(), e);
                return LocalDateTime.MIN;
            } else {
                throw e;
            }
        }
    }


    public static Meal parseMeal(HttpServletRequest request, User user) {
        int id = parseInt(request, MealAttribute.PARAM_ID);
        LocalDateTime dateTime = parseLocalDateTime(request, MealAttribute.PARAM_DATE_TIME);
        String description = parseString(request, MealAttribute.PARAM_DESCRIPTION);
        int mass = parseInt(request, MealAttribute.PARAM_MASS);
        int proteins = parseInt(request, MealAttribute.PARAM_PROTEINS);
        int fats = parseInt(request, MealAttribute.PARAM_FATS);
        int carbohydrates = parseInt(request, MealAttribute.PARAM_CARBOHYDRATES);
        int calories = parseInt(request, MealAttribute.PARAM_CALORIES);

        return createMeal(id, user, dateTime, mass, description, proteins, fats, carbohydrates, calories);
    }

    public static Meal parseMealValid(HttpServletRequest request, User user, @NotNull MealValidation validation) {
        int id = parseInt(request, MealAttribute.PARAM_ID, validation.ID_EMPTY, validation);
        LocalDateTime dateTime = parseLocalDateTime(request, MealAttribute.PARAM_DATE_TIME, validation.DATETIME_EMPTY, validation);
        String description = parseString(request, MealAttribute.PARAM_DESCRIPTION, validation.DESCRIPTION_EMPTY, validation);
        int mass = parseInt(request, MealAttribute.PARAM_MASS, validation.MASS_EMPTY, validation);
        int proteins = parseInt(request, MealAttribute.PARAM_PROTEINS, validation.PROTEINS_EMPTY, validation);
        int fats = parseInt(request, MealAttribute.PARAM_FATS, validation.FATS_EMPTY, validation);
        int carbohydrates = parseInt(request, MealAttribute.PARAM_CARBOHYDRATES, validation.CARBOHYDRATES_EMPTY, validation);
        int calories = parseInt(request, MealAttribute.PARAM_CALORIES, validation.CALORIES_EMPTY, validation);

        if (validation.isNotValid()) {
            log.debug("user parsing with validation has exceptions, return null");
            return null;
        }

        validation.id(MealAttribute.PARAM_ID, id);
        validation.dateTime(MealAttribute.PARAM_DATE_TIME, dateTime);
        validation.description(MealAttribute.PARAM_DESCRIPTION, description);
        validation.params(
                new MealAttribute[]{MealAttribute.PARAM_MASS, MealAttribute.PARAM_PROTEINS, MealAttribute.PARAM_FATS, MealAttribute.PARAM_CARBOHYDRATES, MealAttribute.PARAM_CALORIES},
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

    public static User parseUserValid(HttpServletRequest request, @NotNull UserValidation validation) {
        int id = parseInt(request, UserAttribute.PARAM_USER_ID, validation.USER_ID_EMPTY, validation);
        String name = parseString(request, UserAttribute.PARAM_NAME, validation.NAME_EMPTY, validation);
        int caloriesMin = parseInt(request, UserAttribute.PARAM_CALORIES_MIN, validation.CALORIES_MIN_EMPTY, validation);
        int caloriesMax = parseInt(request, UserAttribute.PARAM_CALORIES_MAX, validation.CALORIES_MAX_EMPTY, validation);

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

    public static UserCredential parseUserCredentialUpdateValid(HttpServletRequest request, User owner, @NotNull UserCredentialValidation validation) {
        String password = parseString(request, UserCredentialAttribute.PARAM_PASSWORD_NEW, validation.PASSWORD_EMPTY, validation);
        String passwordRepeat = parseString(request, UserCredentialAttribute.PARAM_PASSWORD_REPEAT, validation.PASSWORD_REPEAT_EMPTY, validation);

        if (validation.isNotValid()) {
            log.debug("user credential parsing with validation has exceptions, return null");
            return null;
        }

//        validation.login(login);
        validation.password(password);
        validation.passwordRepeatEquals(password, passwordRepeat);

        UserCredential result = createCredential(owner.getId(), null, password);
        if (validation.isNotValid()) {
            log.debug("credential parsing with validation has invalid fields; return INVALID credential");
        } else {
            log.debug("no one invalid fields");
        }

        return result;
    }

    private static void checkEmpty(String str) throws ParamIsEmptyException {
        if (str.isEmpty()) throw new ParamIsEmptyException();
    }

    private static void checkEmpty(String str, Validation validation, String param) throws ParamIsEmptyException {
        try {
            checkEmpty(str);
        } catch (ParamIsEmptyException e) {
            if (validation != null) validation.catchEx(param, e);
            else throw e;
        }
    }
}
