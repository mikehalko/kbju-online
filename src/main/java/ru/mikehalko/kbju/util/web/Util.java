package ru.mikehalko.kbju.util.web;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.parameter.Parameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// TODO exceptions .. = (Class) return .., parse
public class Util {
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
        return (String) request.getAttribute(attribute.value());
    }

    public static String getParameter(HttpServletRequest request, Constant parameter) {
        return request.getParameter(parameter.value());
    }

    public static int getParameterInt(HttpServletRequest request, Constant parameter) {
        return Integer.parseInt(request.getParameter(parameter.value()));
    }
}
