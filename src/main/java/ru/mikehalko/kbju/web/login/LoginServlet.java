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
import ru.mikehalko.kbju.web.exception.BadParameterException;
import ru.mikehalko.kbju.web.exception.EmptyParameterException;
import ru.mikehalko.kbju.web.exception.NotExistParameterException;
import ru.mikehalko.kbju.web.validation.UserCredentialValidator;
import ru.mikehalko.kbju.web.validation.UserValidator;
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

import static ru.mikehalko.kbju.web.util.RequestParameterParser.*;
import static ru.mikehalko.kbju.web.validation.ValidateParser.*;
import static ru.mikehalko.kbju.web.constant.OtherConstant.*;
import static ru.mikehalko.kbju.web.constant.attribute.UserCredentialField.*;
import static ru.mikehalko.kbju.web.constant.parameter.Parameter.*;
import static ru.mikehalko.kbju.web.util.WebUtil.*;

public class LoginServlet extends HttpServlet {
    public static final String HOME_PAGE = "index.html";
    public static final String POST_REDIRECT_LOGIN_ACCESS = "meals" + "?" + ACTION + "=" + ACTION_GET_ALL;
    public static final String POST_REDIRECT_UPDATE_CREDENTIAL = USER + "?" + ACTION + "=" + ACTION_GET;
    public static final String GET_REDIRECT_OUT = HOME_PAGE;
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get");
        String action = null;
        try {
            action = getParameter(request, ACTION);
            switch (Parameter.byValue(action)) {
                case ACTION_LOGIN:
                    log.debug("login; login forward to {}", GET_FORWARD_ACTION_IS_NULL);
                    forward(request, response, GET_FORWARD_ACTION_IS_NULL);
                    break;
                case ACTION_LOGOUT:
                    log.debug("logout; session invalidate, redirect to {}", GET_REDIRECT_OUT);
                    request.getSession().invalidate();
                    response.sendRedirect(GET_REDIRECT_OUT);
                    break;
            }
        } catch (BadParameterException e) {
            sendErrorBadRequest(response, e.getMessage(), this);
            return;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("post");
        try {
            String action = getParameter(request, ACTION);
            log.debug("action = {}", action);
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
        } catch (BadParameterException e) {
            sendErrorBadRequest(response, e.getMessage(), this);
            return;
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        log.debug("login");
        UserCredentialValidator validation = new UserCredentialValidator();
        String passwordHash = null;
        String userLogin = null;
        try {
            try {
                passwordHash = Encryption.hashing(getParameter(request, PARAM_PASSWORD));
                log.debug("password hashed");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (EmptyParameterException e) {
                validation.invalid(PARAM_PASSWORD, "password is empty");
            }

            try {
                userLogin = getParameter(request, PARAM_LOGIN);
            } catch (EmptyParameterException e) {
                validation.invalid(PARAM_LOGIN, "login is empty");
            }
        } catch (NotExistParameterException e) {
            sendErrorBadRequest(response, e.getMessage(), this);
            return;
        }

        if (validation.isNotValid()) {
            setAttribute(request, VALIDATOR_USER_CREDENTIAL, validation);
            forward(request, response, POST_FORWARD_LOGIN_FAIL);
            return;
        }

        UserCredential credential = new UserCredential(userLogin, passwordHash);
        int findId = credentialRepository.find(credential);
        if (findId == 0) {
            log.debug("credential not found");
            validation.invalid(PARAM_LOGIN);
            validation.invalid(PARAM_PASSWORD, "password or login is wrong");
        } else {
            log.debug("credential found for id = {}", findId);
            User user = userRepository.get(findId);
            userSession(session, user, credential.getLogin());
            response.sendRedirect(POST_REDIRECT_LOGIN_ACCESS);
            return;
        }

        if (validation.isNotValid()) {
            setAttribute(request, VALIDATOR_USER_CREDENTIAL, validation);
            forward(request, response, POST_FORWARD_LOGIN_FAIL);
            return;
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response,
                          HttpSession session, boolean loginAfterRegistration) throws ServletException, IOException {
        log.debug("register");
        var userValidation = new UserValidator();
        var credentialValidation = new UserCredentialValidator();
        User userNew = null;
        UserCredential credentialNew = null;
        try {
            userNew = user(request, userValidation);
            credentialNew = credentialNew(request, userNew, credentialValidation);
        } catch (NotExistParameterException e) {
            sendErrorBadRequest(response, e.getMessage(), this);
            return;
        }
        if (!credentialRepository.isUnique(credentialNew.getLogin())) {
            log.debug("login already exist!");
            credentialValidation.invalid(PARAM_LOGIN, String.format("login %s already exist!", credentialNew.getLogin()));
        }
        if (userValidation.isNotValid() || credentialValidation.isNotValid()) {
            log.debug("user or credential invalid");
            setAttribute(request, VALIDATOR_USER, userValidation);
            setAttribute(request, VALIDATOR_USER_CREDENTIAL, credentialValidation);
            setAttribute(request, USER, userNew);
            setAttribute(request, PARAM_LOGIN, credentialNew.getLogin());
            forward(request, response, POST_FORWARD_REGISTER_FAIL);
            return;
        }

        log.debug("validation access");
        try {
            hashingPassword(credentialNew);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // TODO
         }
        var savedUser = userRepository.save(userNew);
        credentialNew.setUser(savedUser);
        boolean accessSaveCredential = credentialRepository.save(credentialNew, savedUser.getId());
        log.debug("user saved id = {}, credential saved = {}; autologin = {} ", savedUser.getId(), accessSaveCredential, loginAfterRegistration);
        if (loginAfterRegistration) {
            userSession(session, savedUser, credentialNew.getLogin());
            response.sendRedirect(POST_REDIRECT_LOGIN_ACCESS);
        } else {
            response.sendRedirect(HOME_PAGE);
        }
    }

    private void userSession(HttpSession session, User user, String login) {
        setAttribute(session, USER, user);
        setAttribute(session, PARAM_LOGIN, login);
    }

    private void hashingPassword(UserCredential credential) throws NoSuchAlgorithmException {
        credential.setPassword(Encryption.hashing(credential.getPassword()));
        log.debug("password hashed");
    }


    private void updatePassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
        log.debug("update password");
        UserCredentialValidator validation = new UserCredentialValidator();
        User user = ServletSecurityUtil.getUserSession(request);
        UserCredential credentialOld = new UserCredential(ServletSecurityUtil.getUserSession(request));
        UserCredential credentialUpdate = null;
        try {
            credentialUpdate =  passwordWithRepeat(request, user, validation);
            hashingPassword(credentialUpdate);
            credentialOld.setPassword(Encryption.hashing(parseString(request, PARAM_PASSWORD_OLD)));
            log.debug("password parse and hashing");
        } catch (NoSuchAlgorithmException | RuntimeException e) {
            log.error("password hashing error");
            throw new RuntimeException(e); // TODO
        } catch (NotExistParameterException | EmptyParameterException e) {
            sendErrorBadRequest(response, e.getMessage(), this);
            return;
        }

        boolean credentialSetLogin = credentialRepository.setLogin(credentialOld);
        if (!credentialSetLogin) {
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
        }

        boolean credentialUpdated = credentialRepository.update(credentialUpdate);
        if (!credentialUpdated) {
            log.error("password new and repeat - valid, but repository returned false: error occurred while updating password");
            response.sendRedirect(HOME_PAGE);
            return;
        }

        log.debug("password updated! Redirect...");
        response.sendRedirect(POST_REDIRECT_UPDATE_CREDENTIAL);
    }
}
