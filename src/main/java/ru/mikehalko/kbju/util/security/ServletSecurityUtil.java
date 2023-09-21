package ru.mikehalko.kbju.util.security;

import ru.mikehalko.kbju.model.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class ServletSecurityUtil {

    public static final String ATTRIBUTE_USER = "user";

    public static int authId(HttpServletRequest request) {
        User user = checkUser(session(request));
        return user.getId();
    }

    public static int authCaloriesPerDay(HttpServletRequest request) {
        User user = checkUser(session(request));
        return user.getCaloriesMax();
    }

    public static int caloriesMin(HttpServletRequest request) {
        User user = checkUser(session(request));
        return user.getCaloriesMin();
    }

    public static int caloriesMax(HttpServletRequest request) {
        User user = checkUser(session(request));
        return user.getCaloriesMax();
    }

    public static User getUserSession(HttpServletRequest request) {
        User user = checkUser(session(request));
        return user;
    }

    public static void setUserSession(HttpServletRequest request, User user) {
        session(request).setAttribute(ATTRIBUTE_USER, user);
    }

    private static HttpSession session(HttpServletRequest request)  {
        HttpSession session = request.getSession(false);
        if (Objects.isNull(session)) throw new RuntimeException("session not found"); // TODO свой exception
        return session;
    }

    private static User checkUser(HttpSession session) {
        User user = (User) session.getAttribute(ATTRIBUTE_USER);
        if (Objects.isNull(user)) throw new RuntimeException("user in session not found"); // TODO свой exception
        return user;
    }
}