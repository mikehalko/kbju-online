package ru.mikehalko.kbju.web.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.model.user.UserCredential;
import ru.mikehalko.kbju.repository.UserCredentialRepository;
import ru.mikehalko.kbju.repository.UserRepository;
import ru.mikehalko.kbju.repository.sql.UserCredentialRepositorySQL;
import ru.mikehalko.kbju.repository.sql.UserRepositorySQL;
import ru.mikehalko.kbju.util.security.ServletSecurityUtil;
import ru.mikehalko.kbju.util.web.RequestParser;
import ru.mikehalko.kbju.util.web.validation.UserCredentialValidation;
import ru.mikehalko.kbju.web.constant.parameter.Parameter;
import ru.mikehalko.kbju.web.cryption.Encryption;
import ru.mikehalko.kbju.web.constant.attribute.UserCredentialAttribute;
import ru.mikehalko.kbju.web.constant.attribute.UserAttribute;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static ru.mikehalko.kbju.util.web.RequestParser.parseString;
import static ru.mikehalko.kbju.web.constant.attribute.OtherAttribute.*;
import static ru.mikehalko.kbju.web.constant.attribute.UserCredentialAttribute.*;
import static ru.mikehalko.kbju.web.constant.parameter.Parameter.*;
import static ru.mikehalko.kbju.util.web.Util.*;

public class LoginServlet extends HttpServlet {
    public static final String INDEX_HTML = "index.html";

    public static final String POST_REDIRECT_REGISTRATION = "/views/user/login";
    public static final String POST_REDIRECT_LOGIN_FAIL = "/views/user/login.jsp";
    public static final String POST_REDIRECT_LOGIN_ACCESS = "meals";
    public static final String POST_REDIRECT_UPDATE_CREDENTIAL = USER + "?" + ACTION + "=" + ACTION_GET;
    public static final String GET_REDIRECT_OUT = INDEX_HTML;
    public static final String GET_FORWARD_ACTION_IS_NULL = "/views/user/login.jsp";

    public static final String POST_FORWARD_LOGIN_FAIL = "/views/user/login.jsp";


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
        log.debug("post");
        String action = getParameter(request, ACTION);
        log.debug("action = {}", action);
        action = action == null ? "" : action; // TODO если action == ""
        HttpSession session = request.getSession();
        switch (Parameter.byValue(action)) {
            case ACTION_LOGIN:
                String userLogin = request.getParameter(PARAM_LOGIN.value());
                String password = request.getParameter(PARAM_PASSWORD.value()); // TODO перенести отсюда внутрь
                login(request, response, session, userLogin, password);
                return;
            case ACTION_REGISTER:
                String userLoginNew = request.getParameter(PARAM_LOGIN.value());
                String passwordNew = request.getParameter(PARAM_PASSWORD_NEW.value());
                String passwordRepeat = request.getParameter(PARAM_PASSWORD_NEW.value());
                String userName = request.getParameter(UserAttribute.PARAM_NAME.value());
                String userCalMin = request.getParameter(UserAttribute.PARAM_CALORIES_MIN.value());
                String userCalMax = request.getParameter(UserAttribute.PARAM_CALORIES_MAX.value()); // TODO перенести в методы
                register(request, response, session, userLoginNew, passwordNew, passwordRepeat,
                        userName, Integer.parseInt(userCalMin), Integer.parseInt(userCalMax), true);
                return;
            case ACTION_UPDATE:
                String passwordUpdateOld = request.getParameter(PARAM_PASSWORD_OLD.value());
                String passwordUpdateNew = request.getParameter(PARAM_PASSWORD_NEW.value()); // TODO перенести отсюда внутрь
                String passwordUpdateRepeat = request.getParameter(UserCredentialAttribute.PARAM_PASSWORD_REPEAT.value());
                updatePassword(request, response, session, passwordUpdateOld, passwordUpdateNew, passwordUpdateRepeat);

                return;
        }

        forward(request, response, INDEX_HTML);
    }

    private void login(HttpServletRequest request, HttpServletResponse response,
                       HttpSession session, String userName, String password) throws ServletException, IOException {
        log.debug("login");
        String passwordHash = null;
        try {
            passwordHash = Encryption.hashing(password);
            log.debug("password hashed");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        UserCredential credential = new UserCredential(userName, passwordHash);
        int findId = credentialRepository.find(credential);
        if (findId != 0) {
            log.debug("credential found for id = {}", findId);
            User user = userRepository.get(findId);
            setAttribute(session, USER, user);
            response.sendRedirect(POST_REDIRECT_LOGIN_ACCESS);
            return;
        } else {
            log.debug("credential not found");
            UserCredentialValidation validation = new UserCredentialValidation();
            validation.invalid(PARAM_LOGIN);
            validation.invalid(PARAM_PASSWORD, "password or login is wrong");
            setAttribute(request, PARAM_PASSWORD, validation);
            fail(request, response, validation, POST_FORWARD_LOGIN_FAIL);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response,
                          HttpSession session, String userLogin, String password, String passwordRepeat,
                          String userName, int calMin, int calMax, boolean loginAfterRegistration) throws ServletException, IOException {
        log.debug("register");
        String passwordHash = null;
        try {
            passwordHash = Encryption.hashing(password);
            log.error("password hashed");
        } catch (NoSuchAlgorithmException e) {
            log.error("password hashing error");
            throw new RuntimeException(e);
        }

        // TODO валидация + валидация уникальности логина. Перенести в другой сервлет?
        UserCredential credential = new UserCredential(userLogin, passwordHash);
        User savedUser = userRepository.save(new User(0, userName, calMin, calMax));
        credentialRepository.save(credential, savedUser.getId());
        log.debug("user and credential saved; autologin = {} ", loginAfterRegistration);
        if (loginAfterRegistration) {
            login(request, response, session, userLogin, password); // register -> login
        } else {
            response.sendRedirect(POST_REDIRECT_REGISTRATION);
        }
    }

    // TODO тоже перенести в другой сервлет?
    private void updatePassword(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                String passwordOld, String passwordNew, String passwordRepeat) throws IOException, ServletException {
        log.debug("update password");
        UserCredentialValidation validation = new UserCredentialValidation();
        User user = ServletSecurityUtil.getUserSession(request);

        // parse credential
        UserCredential credentialUpdate = RequestParser.parseUserCredentialUpdateValid(request, user, validation);
        if (credentialUpdate == null) {
            log.debug("credential update is null");
            fail(request, response, validation, INDEX_HTML);
            return;
        }

        /* TODO вместо все этого?
         TODO - сделать один метод репозитория, который примет старый пароль и новый,
         TODO - затем проверит, что старый существует (иначе вернёт exc, отловив -> невалид),
         TODO - после чего обновит пароль */

        //
        UserCredential credentialOld = new UserCredential(ServletSecurityUtil.authId(request));
        try {
            credentialOld.setPassword(Encryption.hashing(parseString(request, PARAM_PASSWORD_OLD)));
            credentialUpdate.setPassword(Encryption.hashing(credentialUpdate.getPassword()));
            log.debug("password parse and hashing");
        } catch (NoSuchAlgorithmException | RuntimeException e) {
            log.error("password parse or hashing error");
            throw new RuntimeException(e);
        }

        if (!credentialRepository.setLogin(credentialOld)) {
            log.debug("invalid password, fail set login for credential");
            validation.appendWithSeparator("invalid password"); // TODO перенос в validation, сделать invalid(field, message)
            validation.invalid(PARAM_PASSWORD);
        } else {
            log.debug("credential founded");
            credentialUpdate.setLogin(credentialOld.getLogin());
        }

        if (validation.isNotValid()) {
            log.debug("credential invalid -> fail()");
            setAttribute(request, USER_EDIT, user);
            fail(request, response, validation, "views/user/update.jsp"); // TODO заменить на константу
            return;
        } else {
            if (!credentialRepository.update(credentialUpdate)) {
                log.error("password new and repeat - valid, but repository returned false: error occurred while updating password");
            }
            log.debug("password updated!");
        }

        response.sendRedirect(POST_REDIRECT_UPDATE_CREDENTIAL);
    }

    // Можно принять нужный путь
    private void fail(HttpServletRequest request, HttpServletResponse response,
                      UserCredentialValidation validation, String path) throws ServletException, IOException {
        log.debug("fail");
        setAttribute(request, validation.attribute(), validation);
        forward(request, response, path); // TODO вынести в константу
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get");
        String action = getParameter(request, ACTION);
        if (action == null) {
            log.debug("action is null, login forward");
            forward(request, response, GET_FORWARD_ACTION_IS_NULL);
            return;
        }

        // TODO action.isEmpty()
        switch (Parameter.byValue(action)) {
            case ACTION_LOGOUT:
                log.debug("logout");
                request.getSession().invalidate();
                log.debug("session invalidate, redirect to " + GET_REDIRECT_OUT);
                response.sendRedirect(GET_REDIRECT_OUT);
                break;
            default:
                log.debug("default");
        }
    }
}
