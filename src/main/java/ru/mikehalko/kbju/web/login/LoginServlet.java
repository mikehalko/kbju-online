package ru.mikehalko.kbju.web.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.user.UserCredential;
import ru.mikehalko.kbju.repository.UserCredentialRepository;
import ru.mikehalko.kbju.repository.UserRepository;
import ru.mikehalko.kbju.repository.sql.UserCredentialRepositorySQL;
import ru.mikehalko.kbju.repository.sql.UserRepositorySQL;
import ru.mikehalko.kbju.web.cryption.Encryption;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static ru.mikehalko.kbju.web.constant.UserParams.*;

public class LoginServlet extends HttpServlet {
    public static final String PARAM_ACTION = "action";

    public static final String ATTRIBUTE_USER = "user";
    public static final String PARAM_LOGIN = "login";
    public static final String PARAM_PASSWORD = "password";

    public static final String INDEX_HTML = "index.html";

    public static final String POST_REDIRECT_REGISTRATION = "/views/user/login";
    public static final String POST_REDIRECT_LOGIN_FAIL = "/views/user/login?msg=loginFail";
    public static final String POST_REDIRECT_LOGIN_ACCESS = "meals";
    public static final String GET_REDIRECT_OUT = INDEX_HTML;
    public static final String GET_FORWARD_ACTION_IS_NULL = "/views/user/login.jsp";

    private final Logger log = LoggerFactory.getLogger(LoginServlet.class);
    private static UserRepository userRepository;
    private static UserCredentialRepository credentialRepository;


    @Override
    public void init(ServletConfig config) throws ServletException {
        userRepository = UserRepositorySQL.getInstance();
        credentialRepository = UserCredentialRepositorySQL.getInstance();
        super.init(config);
    }

    // TODO валидация полей, валидация повтора пароля
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userLogin = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        String action = request.getParameter(PARAM_ACTION);
        log.debug("login = {}, pass = {}, action = {}", userLogin, password, action); // TODO убрать password
        HttpSession session = request.getSession(); // !
        switch (action) {
            case "login":
                login(request, response, session, userLogin, password);
                return;
            case "register":
                String userName = request.getParameter(PARAM_NAME.value());
                String userCalMin = request.getParameter(PARAM_CALORIES_MIN.value());
                String userCalMax = request.getParameter(PARAM_CALORIES_MAX.value());
                register(request, response, session, userLogin, password,
                        userName, Integer.parseInt(userCalMin), Integer.parseInt(userCalMax), true);
                return;
        }

        request.getRequestDispatcher(INDEX_HTML).forward(request, response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response,
                       HttpSession session, String userName, String password) throws ServletException, IOException {
        String passwordHash = null;
        try {
            passwordHash = Encryption.hashing(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        UserCredential credential = new UserCredential(userName, passwordHash);
        log.debug("credential = {}", credential);
        int findId = credentialRepository.find(credential);
        if (findId != 0) {
            User user = userRepository.get(findId);
            session.setAttribute(ATTRIBUTE_USER, user);
            log.debug("user founded, id = {}", findId);
            response.sendRedirect(POST_REDIRECT_LOGIN_ACCESS);
            return;
        } else {
            response.sendRedirect(POST_REDIRECT_LOGIN_FAIL);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response,
                          HttpSession session, String userLogin, String password,
                          String userName, int calMin, int calMax, boolean loginAfterRegistration) throws ServletException, IOException {
        String passwordHash = null;
        try {
            passwordHash = Encryption.hashing(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // TODO валидация уникальности
        UserCredential credential = new UserCredential(userLogin, passwordHash);
        log.debug("credential = {}", credential);
        User user = new User(0, userName, calMin, calMax);
        user = userRepository.save(user);
        log.debug("new user saved = {}", user);
        credentialRepository.save(credential, user.getId());
        log.debug("credential saved");
        if (loginAfterRegistration) {
            login(request, response, session, userLogin, password); // register -> login
        } else {
            response.sendRedirect(POST_REDIRECT_REGISTRATION);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get");
        String action = request.getParameter(PARAM_ACTION);
        if (action == null) {
            request.getRequestDispatcher(GET_FORWARD_ACTION_IS_NULL).forward(request, response);
            log.debug("action is null,  login forward");
            return;
        }

        switch (action) {
            case "out":
                log.debug("session invalidate, redirect to " + GET_REDIRECT_OUT);
                request.getSession().invalidate();
                response.sendRedirect(GET_REDIRECT_OUT);
                break;
            default:
                log.debug("default");
        }
    }
}
