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
import ru.mikehalko.kbju.util.web.validation.UserCredentialValidator;
import ru.mikehalko.kbju.util.web.validation.UserValidator;
import ru.mikehalko.kbju.web.constant.parameter.Parameter;
import ru.mikehalko.kbju.web.cryption.Encryption;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static ru.mikehalko.kbju.util.web.RequestParser.parseString;
import static ru.mikehalko.kbju.web.constant.OtherConstant.*;
import static ru.mikehalko.kbju.web.constant.attribute.UserCredentialField.*;
import static ru.mikehalko.kbju.web.constant.parameter.Parameter.*;
import static ru.mikehalko.kbju.util.web.Util.*;

public class LoginServlet extends HttpServlet {
    public static final String INDEX_HTML = "index.html";

    public static final String POST_REDIRECT_REGISTRATION = "/views/user/login";
    public static final String POST_REDIRECT_LOGIN_ACCESS = "meals";
    public static final String POST_REDIRECT_UPDATE_CREDENTIAL = USER + "?" + ACTION + "=" + ACTION_GET; // TODO подумать об этом
    public static final String GET_REDIRECT_OUT = INDEX_HTML;
    public static final String GET_FORWARD_ACTION_IS_NULL = "/views/user/login.jsp";

    public static final String POST_FORWARD_LOGIN_FAIL = "/views/user/login.jsp";
    public static final String POST_FORWARD_REGISTER_FAIL = "/views/user/login.jsp";
    public static final String POST_FORWARD_CREDENTIAL_UPDATE_FAIL = "/views/user/update.jsp";


    private final Logger log = LoggerFactory.getLogger(LoginServlet.class);
    private static UserRepository userRepository;
    private static UserCredentialRepository credentialRepository;


    @Override
    public void init(ServletConfig config) throws ServletException {
        userRepository = UserRepositorySQL.getInstance();
        credentialRepository = UserCredentialRepositorySQL.getInstance();
        super.init(config);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("post");
        String action = getParameter(request, ACTION);
        log.debug("action = {}", action);
        action = action == null ? "" : action.isEmpty() ? "" : action;
        HttpSession session = request.getSession();
        switch (Parameter.byValue(action)) {
            case ACTION_LOGIN:
                login(request, response, session);
                return;
            case ACTION_REGISTER:
                 register(request, response, session, true);
                return;
            case ACTION_UPDATE:
                updatePassword(request, response, session);
                return;
        }

        forward(request, response, INDEX_HTML);
    }

    private void login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        String passwordHash = null;
        try {
            passwordHash = Encryption.hashing(getParameter(request, PARAM_PASSWORD));
            log.debug("password hashed");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        login(request, response, session, passwordHash);
    }

    private void login(HttpServletRequest request, HttpServletResponse response, HttpSession session, String passwordHash) throws ServletException, IOException {
        log.debug("login");
        String userLogin = getParameter(request, PARAM_LOGIN);
        UserCredential credential = new UserCredential(userLogin, passwordHash);
        int findId = credentialRepository.find(credential);
        if (findId != 0) {
            log.debug("credential found for id = {}", findId);
            User user = userRepository.get(findId);
            setAttribute(session, USER, user);
            response.sendRedirect(POST_REDIRECT_LOGIN_ACCESS);
            return;
        } else {
            log.debug("credential not found");
            UserCredentialValidator validation = new UserCredentialValidator();
            validation.invalid(PARAM_LOGIN);
            validation.invalid(PARAM_PASSWORD, "password or login is wrong");
            setAttribute(request, VALIDATOR_USER_CREDENTIAL, validation);
            forward(request, response, POST_FORWARD_LOGIN_FAIL);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response,
                          HttpSession session, boolean loginAfterRegistration) throws ServletException, IOException {
        log.debug("register");
        var userValidation = new UserValidator();
        var credentialValidation = new UserCredentialValidator();
        User userNew = RequestParser.user(request, userValidation);
        UserCredential credentialNew = RequestParser.credentialNew(request, userNew, credentialValidation);
        if (Objects.isNull(userNew) || Objects.isNull(credentialNew)) {
            log.error("userNew or credentialNew == null");
            throw new RuntimeException();
        }
        // проверить уникальность логина
        if (!credentialRepository.isUnique(credentialNew.getLogin())) {
            log.debug("login already exist!");
            credentialValidation.invalid(PARAM_LOGIN, String.format("login %s already exist!", credentialNew.getLogin()));
        }
        if (userValidation.isNotValid() || credentialValidation.isNotValid()) {
            log.debug("user or credential invalid");
            setAttribute(request, VALIDATOR_USER, userValidation);
            setAttribute(request, VALIDATOR_USER_CREDENTIAL, credentialValidation);
            // положить в атрибут объекты для повторной формы
            setAttribute(request, USER, userNew);
            setAttribute(request, PARAM_LOGIN, credentialNew.getLogin());
            forward(request, response, POST_FORWARD_REGISTER_FAIL);
            return;
        }
        // < валидация пройдена >
        hashingPassword(credentialNew);

        // сохранить
        var savedUser = userRepository.save(userNew);
        credentialNew.setUser(savedUser);
        boolean accessSaveCredential = credentialRepository.save(credentialNew, savedUser.getId());
        log.debug("user saved, credential saved = {}; autologin = {} ", accessSaveCredential, loginAfterRegistration);
        if (loginAfterRegistration) {
            login(request, response, session, credentialNew.getPassword()); // register -> login
        } else {
            response.sendRedirect(POST_REDIRECT_REGISTRATION);
        }
    }

    private void hashingPassword(UserCredential credential) {
        try {
            credential.setPassword(Encryption.hashing(credential.getPassword()));
            log.error("password hashed");
        } catch (NoSuchAlgorithmException e) {
            log.error("password hashing error");
            throw new RuntimeException(e);
        }
    }


    private void updatePassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
        log.debug("update password");
        UserCredentialValidator validation = new UserCredentialValidator();
        User user = ServletSecurityUtil.getUserSession(request);

        // parse credential
        UserCredential credentialUpdate = RequestParser.passwordWithRepeat(request, user, validation);
        if (credentialUpdate == null) {
            log.debug("credential update is null");
            setAttribute(request, VALIDATOR_USER_CREDENTIAL, validation);
            forward(request, response, INDEX_HTML);
            return;
        }

        /* TODO вместо все этого?
          - сделать один метод репозитория, который примет старый пароль и новый,
          - затем проверит, что старый существует (иначе вернёт exc, отловив -> невалид),
          - после чего обновит пароль */

        UserCredential credentialOld = new UserCredential(ServletSecurityUtil.getUserSession(request));
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
            validation.invalid(PARAM_PASSWORD, "invalid password");
        } else {
            log.debug("credential founded");
            credentialUpdate.setLogin(credentialOld.getLogin());
        }

        if (validation.isNotValid()) {
            log.debug("credential invalid -> fail()");
            setAttribute(request, USER_EDIT, user);
            setAttribute(request, VALIDATOR_USER_CREDENTIAL, validation);
            forward(request, response, POST_FORWARD_CREDENTIAL_UPDATE_FAIL);
            return;
        } else {
            if (!credentialRepository.update(credentialUpdate)) {
                log.error("password new and repeat - valid, but repository returned false: error occurred while updating password");
            }
            log.debug("password updated!");
        }

        response.sendRedirect(POST_REDIRECT_UPDATE_CREDENTIAL);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get");
        String action = getParameter(request, ACTION);
        if (action == null) {
            log.debug("action is null, login forward");
            forward(request, response, GET_FORWARD_ACTION_IS_NULL);
            return;
        }

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
