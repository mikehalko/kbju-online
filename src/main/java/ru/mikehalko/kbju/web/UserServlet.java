package ru.mikehalko.kbju.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.repository.UserRepository;
import ru.mikehalko.kbju.repository.sql.UserRepositorySQL;
import ru.mikehalko.kbju.util.security.ServletSecurityUtil;
import ru.mikehalko.kbju.util.web.validation.UserValidation;
import ru.mikehalko.kbju.web.constant.OtherParams;
import ru.mikehalko.kbju.web.constant.UserParams;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.mikehalko.kbju.util.web.RequestParser.parseString;
import static ru.mikehalko.kbju.util.web.RequestParser.parseUserValid;
import static ru.mikehalko.kbju.web.constant.OtherParams.PARAM_ACTION;

public class UserServlet extends HttpServlet {
    // show, update
    public static final String ATTRIBUTE_USER = "user_edit";
    public static final String ATTRIBUTE_VALIDATION = "validator";

    public static final String PARAM_ACTION_GET = "get";
    public static final String PARAM_ACTION_UPDATE = "update";

    public static final String GET_FORWARD_SHOW = "views/user/show.jsp";
    public static final String GET_FORWARD_UPDATE = "views/user/update.jsp";
    public static final String POST_REDIRECT_AFTER_UPDATE = "user?action=get";

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);
    private static UserRepository userRepository;

    private static final User mockUser = new User(0, ""); // TODO mock

    @Override
    public void init(ServletConfig config) throws ServletException {
        userRepository = UserRepositorySQL.getInstance();
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet");
        User user = ServletSecurityUtil.getUserSession(request);
        request.setAttribute(ATTRIBUTE_USER, user);

        String action = parseString(request, PARAM_ACTION);
        if (action.isEmpty()) action = PARAM_ACTION_GET;
        switch (action) {
            case PARAM_ACTION_GET:
                log.debug("show = {}", user);
                request.getRequestDispatcher(GET_FORWARD_SHOW).forward(request, response);
                break;
            case PARAM_ACTION_UPDATE:
                log.debug("update = {}", user);
                // TODO или здесь надо обнулить атрибут валидации
                request.setAttribute(ATTRIBUTE_VALIDATION, null);
                request.getRequestDispatcher(GET_FORWARD_UPDATE).forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost");

        UserValidation validation = new UserValidation();
        User updatedUser = parseUserValid(request, validation);
        if (updatedUser == null) updatedUser = mockUser;

        log.debug("VALIDATION = {}", validation.isValid());
        if (validation.isNotValid()) {
            failPost(validation, request, response, updatedUser);
            return;
        }
        // TODO здесь надо обнулить атрибут валидации


        if (ServletSecurityUtil.getUserSession(request).getId() == updatedUser.getId()) {
            ServletSecurityUtil.setUserSession(request, userRepository.save(updatedUser));
            log.debug("save {}", updatedUser);
        } else {
            log.error("auth.id != updated.id");
        }
        response.sendRedirect(POST_REDIRECT_AFTER_UPDATE);
    }

    private void failPost(UserValidation validation, HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String message = validation.resultMessage();
        log.debug("invalid data form = {}", message);
        log.debug("set meal form = {}", user); // TODO убрать
        request.setAttribute(ATTRIBUTE_VALIDATION, validation);
        request.setAttribute(ATTRIBUTE_USER, user);

//        String action = request.getParameter(PARAM_ACTION.value()); // TODO убрать...
        log.debug("fail message = {}", message);
        log.debug("valid fields = id:{}, name:{}, min:{}, max:{}",
                validation.isValid(UserParams.PARAM_USER_ID),
                validation.isValid(UserParams.PARAM_NAME),
                validation.isValid(UserParams.PARAM_CALORIES_MIN),
                validation.isValid(UserParams.PARAM_CALORIES_MAX)
                ); // TODO убрать !!!

        log.debug("update forward after fail");
        request.getRequestDispatcher(GET_FORWARD_UPDATE).forward(request, response);
    }
}
