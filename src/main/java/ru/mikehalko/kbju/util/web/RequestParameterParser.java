package ru.mikehalko.kbju.util.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.util.web.exception.NotExistParameterException;
import ru.mikehalko.kbju.util.web.exception.EmptyParameterException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.attribute.*;


// TODO тесты
public class RequestParameterParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParameterParser.class);

    public static String parseString(HttpServletRequest request, Constant param)
            throws EmptyParameterException, NotExistParameterException {
        String result = request.getParameter(param.value());
        if (result == null) {
            log.debug("parameter is null");
            throw new NotExistParameterException(param);
        }
        if (result.isEmpty()) {
            log.debug("parameter is empty");
            throw new EmptyParameterException(param);
        }
        return result;
    }

    public static int parseInt(HttpServletRequest request, Constant param)
            throws EmptyParameterException, NotExistParameterException, NumberFormatException  {
        return Integer.parseInt(parseString(request, param));
    }

    public static LocalDateTime localDateTime(HttpServletRequest request, MealField param)
            throws EmptyParameterException, NotExistParameterException, DateTimeParseException {
        return LocalDateTime.parse(parseString(request, param));
    }
//
//    public static Meal meal(HttpServletRequest request, User user)
//            throws ParameterNotExistException, ParameterIsEmptyException {
//        int id = parseInt(request, MealField.PARAM_ID);
//        LocalDateTime dateTime = localDateTime(request, MealField.PARAM_DATE_TIME);
//        String description = parseString(request, MealField.PARAM_DESCRIPTION);
//        int mass = parseInt(request, MealField.PARAM_MASS);
//        int proteins = parseInt(request, MealField.PARAM_PROTEINS);
//        int fats = parseInt(request, MealField.PARAM_FATS);
//        int carbohydrates = parseInt(request, MealField.PARAM_CARBOHYDRATES);
//        int calories = parseInt(request, MealField.PARAM_CALORIES);
//
//        return createMeal(id, user, dateTime, mass, description, proteins, fats, carbohydrates, calories);
//    }
}
