package ru.mikehalko.kbju.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.repository.UserRepository;
import ru.mikehalko.kbju.repository.sql.UserCredentialRepositorySQL;
import ru.mikehalko.kbju.repository.sql.UserRepositorySQL;
import ru.mikehalko.kbju.util.security.ServletSecurityUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static ru.mikehalko.kbju.util.model.UserUtil.createUser;

public class UserServlet extends HttpServlet {
    // show, update
    public static final String ATTRIBUTE_USER = "user";
    public static final String PARAM_ACTION = "action";
    public static final String PARAM_ACTION_GET = "get";
    public static final String PARAM_ACTION_UPDATE = "update";

    public static final String PARAM_ID = "user_id";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_CALORIES_MIN = "calories_min";
    public static final String PARAM_CALORIES_MAX = "calories_max";

    public static final String GET_FORWARD_SHOW = "views/user/show.jsp";
    public static final String GET_FORWARD_UPDATE = "views/user/update.jsp";
    public static final String POST_REDIRECT_AFTER_UPDATE = "meals";

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);
    private static UserRepository userRepository;

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

        String action = parseStringSafe(request, PARAM_ACTION);
        if (action.isEmpty()) action = PARAM_ACTION_GET;
        switch (action) {
            case PARAM_ACTION_GET:
                log.debug("show = {}", user);
                request.getRequestDispatcher(GET_FORWARD_SHOW).forward(request, response);
                break;
            case PARAM_ACTION_UPDATE:
                log.debug("update = {}", user);
                request.getRequestDispatcher(GET_FORWARD_UPDATE).forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost");

        User updatedUser = parseUser(request);
        if (ServletSecurityUtil.getUserSession(request).getId() == updatedUser.getId()) {
            ServletSecurityUtil.setUserSession(request, userRepository.save(updatedUser));
            log.debug("save {}", updatedUser);
        } else {
            log.debug("auth.id != updated.id");
        }
        response.sendRedirect(POST_REDIRECT_AFTER_UPDATE);
    }

    public static int parseIntSafe(HttpServletRequest request, String param) {
        String id = getSafe(request.getParameter(param));
        return id.isEmpty() ? 0 : Integer.parseInt(id);
    }

    public static String parseStringSafe(HttpServletRequest request, String param) {
        return getSafe(request.getParameter(param));
    }

    private static User parseUser(HttpServletRequest request) {
        int id = parseIntSafe(request, PARAM_ID);
        String name = parseStringSafe(request,PARAM_NAME);
        int caloriesMin = parseIntSafe(request, PARAM_CALORIES_MIN);
        int caloriesMax = parseIntSafe(request, PARAM_CALORIES_MAX);

        return createUser(id, name, caloriesMin, caloriesMax);
    }

    private static String getSafe(String string) {
        return string == null ? "" : string; // TODO убрать эти метода отсюда
    }
}
