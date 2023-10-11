package ru.mikehalko.kbju.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.web.exception.NotExistParameterException;
import ru.mikehalko.kbju.web.exception.EmptyParameterException;

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
}
