package ru.mikehalko.kbju.web.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.user.UserCredential;
import ru.mikehalko.kbju.web.util.RequestParameterParser;
import ru.mikehalko.kbju.web.exception.EmptyParameterException;
import ru.mikehalko.kbju.web.exception.NotExistParameterException;
import ru.mikehalko.kbju.web.constant.attribute.FieldAttribute;
import ru.mikehalko.kbju.web.constant.attribute.MealField;
import ru.mikehalko.kbju.web.constant.attribute.UserCredentialField;
import ru.mikehalko.kbju.web.constant.attribute.UserField;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static ru.mikehalko.kbju.util.model.MealsUtil.createMeal;
import static ru.mikehalko.kbju.util.model.UserCredentialUtil.createCredential;
import static ru.mikehalko.kbju.util.model.UserUtil.createUser;

public class ValidateParser {
    private static final Logger log = LoggerFactory.getLogger(ValidateParser.class);

    public static <T extends FieldAttribute> String parseString(HttpServletRequest request, T field, Validator<T> validation)
            throws NotExistParameterException {
        try {
            return RequestParameterParser.parseString(request, field);
        } catch (EmptyParameterException e) {
            validation.empty(field);
            return "";
        }
    }

    public static <T extends FieldAttribute> int parseInt(HttpServletRequest request, T field, Validator<T> validation)
            throws NotExistParameterException, NumberFormatException {
        try {
            return RequestParameterParser.parseInt(request, field);
        } catch (EmptyParameterException e) {
            validation.empty(field);
            return 0;
        } catch (NumberFormatException e) {
            log.error("{} format is wrong!", field);
            validation.invalid(field, "format is wrong");
            return 0;
        }
    }

    public static <T extends FieldAttribute> LocalDateTime localDateTime(HttpServletRequest request, T field, Validator<T> validation)
            throws NotExistParameterException, DateTimeParseException {
        try {
            return LocalDateTime.parse(RequestParameterParser.parseString(request, field));
        } catch (EmptyParameterException e) {
            validation.empty(field);
            return null;
        } catch (DateTimeParseException e) {
            log.error("{} format is wrong!", field);
            validation.invalid(field, "format is wrong");
            return null;
        }
    }

    public static Meal meal(HttpServletRequest request, User user, @NotNull MealValidator validation)
            throws NotExistParameterException {
        int id = parseInt(request, MealField.PARAM_ID, validation);
        LocalDateTime dateTime = localDateTime(request, MealField.PARAM_DATE_TIME, validation);
        String description = parseString(request, MealField.PARAM_DESCRIPTION, validation);
        int mass = parseInt(request, MealField.PARAM_MASS, validation);
        int proteins = parseInt(request, MealField.PARAM_PROTEINS, validation);
        int fats = parseInt(request, MealField.PARAM_FATS, validation);
        int carbohydrates = parseInt(request, MealField.PARAM_CARBOHYDRATES, validation);
        int calories = parseInt(request, MealField.PARAM_CALORIES, validation);

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

    public static User user(HttpServletRequest request, @NotNull UserValidator validation)
            throws NotExistParameterException {
        int id = parseInt(request, UserField.PARAM_USER_ID, validation);
        String name = parseString(request, UserField.PARAM_NAME, validation);
        int caloriesMin = parseInt(request, UserField.PARAM_CALORIES_MIN, validation);
        int caloriesMax = parseInt(request, UserField.PARAM_CALORIES_MAX, validation);

        if (validation.isNotValid()) {
            log.debug("user parsing-validation has exceptions");
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

    public static UserCredential credentialNew(HttpServletRequest request, User owner, @NotNull UserCredentialValidator validation)
            throws NotExistParameterException {
        String login = parseString(request, UserCredentialField.PARAM_LOGIN, validation);
        if (validation.isNotValid()) {
            log.debug("\"{}\" parsing (with validation) has exceptions", UserCredentialField.PARAM_LOGIN);
        }
        UserCredential result = credentialPasswordWithRepeat(request, owner, validation);
        result.setLogin(login);
        validation.login(login);

        if (validation.isNotValid()) {
            log.debug("credential parsing with validation has invalid fields; return INVALID credential");
        } else {
            log.debug("no one invalid fields");
        }

        return result;
    }

    public static UserCredential passwordWithRepeat(HttpServletRequest request, User owner, @NotNull UserCredentialValidator validation)
            throws NotExistParameterException {
        UserCredential result = credentialPasswordWithRepeat(request, owner, validation);
        if (validation.isNotValid()) {
            log.debug("credential parsing with validation has invalid fields; return INVALID credential");
        } else {
            log.debug("no one invalid fields");
        }
        return result;
    }

    private static UserCredential credentialPasswordWithRepeat(HttpServletRequest request, User owner, @NotNull UserCredentialValidator validation)
            throws NotExistParameterException {
        String password = parseString(request, UserCredentialField.PARAM_PASSWORD_NEW, validation);
        String passwordRepeat = parseString(request, UserCredentialField.PARAM_PASSWORD_REPEAT, validation);

        if (validation.isNotValid()) {
            log.debug("\"{}\" \"{}\" parsing (with validation) has exceptions, return INVALID",
                    UserCredentialField.PARAM_PASSWORD_NEW, UserCredentialField.PARAM_PASSWORD_REPEAT);
        }
        validation.password(password);
        validation.passwordRepeatEquals(password, passwordRepeat);
        return createCredential(owner, null, password);
    }
}
