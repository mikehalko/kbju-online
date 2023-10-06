package ru.mikehalko.kbju.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.mock.UserMock;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.repository.UserRepository;
import ru.mikehalko.kbju.repository.sql.UserRepositorySQL;
import ru.mikehalko.kbju.util.security.ServletSecurityUtil;
import ru.mikehalko.kbju.util.web.validation.UserValidation;
import ru.mikehalko.kbju.web.constant.parameter.Parameter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.mikehalko.kbju.util.web.RequestParser.parseString;
import static ru.mikehalko.kbju.util.web.RequestParser.user;
import static ru.mikehalko.kbju.util.web.Util.*;
import static ru.mikehalko.kbju.web.constant.attribute.OtherAttribute.*;
import static ru.mikehalko.kbju.web.constant.parameter.Parameter.*;

public class UserServlet extends HttpServlet {
    public static final String GET_FORWARD_SHOW = "views/user/show.jsp";
    public static final String GET_FORWARD_UPDATE = "views/user/update.jsp";
    public static final String POST_REDIRECT_AFTER_UPDATE = SERVLET_USER + "?" + ACTION + "=" + ACTION_GET;

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);
    private static UserRepository userRepository;

    private static final User mockUser = new UserMock();

    @Override
    public void init(ServletConfig config) throws ServletException {
        userRepository = UserRepositorySQL.getInstance();
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet");
        User user = ServletSecurityUtil.getUserSession(request);
        setAttribute(request, USER_EDIT, user);

        String action = parseString(request, ACTION); // TODO action null -> свой exception с ответом стр не найдена неверный запрос
        if (action == null || action.isEmpty()) action = ACTION_GET.value();
        switch (Parameter.byValue(action)) {
            case ACTION_GET:
                log.debug("action {} for user = {}", action, user);
                forward(request, response, GET_FORWARD_SHOW);
                break;
            case ACTION_UPDATE:
                log.debug("action {} for user = {}", action, user);
                forward(request, response, GET_FORWARD_UPDATE);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost");

        UserValidation validation = new UserValidation();
        User updatedUser = user(request, validation);
        if (updatedUser == null) updatedUser = mockUser;

        log.debug("VALIDATION = {}", validation.isValid());
        if (validation.isNotValid()) {
            failPost(validation, request, response, updatedUser);
            return;
        }

        if (ServletSecurityUtil.getUserSession(request).getId() == updatedUser.getId()) {
            ServletSecurityUtil.setUserSession(request, userRepository.save(updatedUser));
            log.debug("save {}", updatedUser);
        } else {
            log.error("auth.id != updated.id");
        }
        response.sendRedirect(POST_REDIRECT_AFTER_UPDATE);
    }

    private void failPost(UserValidation validation, HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        log.debug("invalid data form = {}", validation.resultMessage());
        setAttribute(request, VALIDATOR_USER, validation);
        setAttribute(request, USER_EDIT, user);
        forward(request, response, GET_FORWARD_UPDATE);
    }
}
