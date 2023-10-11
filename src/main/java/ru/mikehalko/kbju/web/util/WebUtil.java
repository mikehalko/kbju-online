package ru.mikehalko.kbju.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.web.exception.EmptyParameterException;
import ru.mikehalko.kbju.web.exception.NotExistParameterException;
import ru.mikehalko.kbju.web.constant.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// TODO exceptions .. = (Class) return .., parse
public class WebUtil {
    private static final Logger log = LoggerFactory.getLogger(WebUtil.class);

    public static void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }

    public static void setAttribute(HttpSession session, Constant attribute, Object object) {
        session.setAttribute(attribute.value(), object);
    }

    public static void setAttribute(HttpServletRequest request, Constant attribute, Object object) {
        request.setAttribute(attribute.value(), object);
    }

    public static Object getAttribute(HttpServletRequest request, Constant attribute) {
        return request.getAttribute(attribute.value());
    }

    public static String getAttributeString(HttpServletRequest request, Constant attribute) {
        Object object = getAttribute(request, attribute);
        if (object == null) {} // TODO exception
        String result = null;
        try {
            result = (String) object;
        } catch (Exception e) {
            // TODO exception
            throw e;
        }
        return result;
    }

    public static String getParameter(HttpServletRequest request, Constant parameter) throws NotExistParameterException, EmptyParameterException {
        return RequestParameterParser.parseString(request, parameter);
    }

    public static int getParameterInt(HttpServletRequest request, Constant parameter) throws NotExistParameterException, EmptyParameterException {
        return RequestParameterParser.parseInt(request, parameter);
    }

    public static void sendErrorBadRequest(HttpServletResponse response, String message, HttpServlet servlet) throws IOException {
        log.debug("bad request from servlet = {}", servlet.getServletName());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
    }
}
