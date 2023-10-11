package ru.mikehalko.kbju.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.util.security.ServletSecurityUtil;
import ru.mikehalko.kbju.util.web.WebUtil;
import ru.mikehalko.kbju.util.web.exception.BadParameterException;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static java.util.Objects.nonNull;
import static ru.mikehalko.kbju.util.security.ServletSecurityUtil.ATTRIBUTE_USER;
import static ru.mikehalko.kbju.web.constant.parameter.Parameter.*;

public class AuthFilter extends HttpFilter {
    private final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    public static final String POST_REDIRECT_LOGIN_ALREADY = "meals" + "?" + ACTION + "=" + ACTION_GET_ALL;
    public static final String GUEST_REDIRECT = "login?" + ACTION + "=" + ACTION_LOGIN;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/login";

        boolean logged = nonNull(session) && nonNull(session.getAttribute(ATTRIBUTE_USER));
        boolean loginRequest = request.getRequestURI().equals(loginURI);
        if (logged) {
            logged(request, response, chain, loginRequest);
            return;
        }
        notLogged(request, response, chain, loginRequest);
        return;
    }

    private void logged(HttpServletRequest request, HttpServletResponse response, FilterChain chain, boolean loginRequest) throws ServletException, IOException {
        User user = ServletSecurityUtil.getUserSession(request);
        if (loginRequest) {
            try {
                String action = WebUtil.getParameter(request, ACTION);
                log.debug("action = {}", action);
                boolean wantToOut = action.equals(ACTION_LOGOUT.value());
                boolean wantToUpdateCredential = action.equals(ACTION_UPDATE.value());
                if (wantToOut || wantToUpdateCredential) {
                    log.debug("user[{}] want to out = {}, update credential = {}, goes on", user.getId(), wantToOut, wantToUpdateCredential);
                    chain.doFilter(request, response);
                    return;
                }
            } catch (BadParameterException badActionExceptionIgnored) {
                log.debug("action is null or empty");
            }
            log.debug("user[{}] already login and want going to login page. Redirect to meals", user.getId());
            WebUtil.forward(request, response, POST_REDIRECT_LOGIN_ALREADY);
            return;
        }

        chain.doFilter(request, response);
    }

    private void notLogged(HttpServletRequest request, HttpServletResponse response, FilterChain chain, boolean loginRequest)
            throws ServletException, IOException {
        if (loginRequest) {
            log.debug("guest want to login");
            chain.doFilter(request, response);
        } else {
            log.debug("guest does not have access, redirect to home page");
            response.sendRedirect(GUEST_REDIRECT);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}