package ru.mikehalko.kbju.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.model.user.User;
import ru.mikehalko.kbju.web.Context;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import static java.util.Objects.nonNull;
import static ru.mikehalko.kbju.util.sql.ConnectDataBase.*;

//Все страницы сайта обрабатывает данный фильтр
public class AuthFilter extends HttpFilter {
    private final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        try {
            // TODO ловить исключение в репозиториях перед отправкой запроса? Чтобы переподключиться в тот момент
            reconnectIfNeed(getConnectionHold(), Context.getHavingConnection());
        } catch (SQLException | ClassNotFoundException e) {
            log.debug("checkConnection fail");
            throw new RuntimeException(e);
        }
        //получение данных сессии
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/login";
        log.debug("request URI = {}", request.getRequestURI());
        boolean logged = nonNull(session) && nonNull(session.getAttribute("user")); /*&& nonNull(session.getAttribute("userName")) && nonNull(session.getAttribute("userRole"));*/
        boolean loginRequest = request.getRequestURI().equals(loginURI);
        log.debug("logged? {}, login request? {}", logged, loginRequest);

        if (logged) logged(request, response, chain, session, loginRequest);
        else notLogged(request, response, chain, loginRequest);
    }

    private void logged(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                        HttpSession session, boolean loginRequest) throws ServletException, IOException {
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        boolean wantToOut = nonNull(action) && action.equals("out");

        if (wantToOut) {
            log.debug("user[{}] want to out", user);
            chain.doFilter(request, response);
        } else if (loginRequest) {
            log.debug("user[{}] already login and want going to login page. Redirect to meals", user);
            request.getRequestDispatcher("meals").forward(request, response); // в index.html прятать ссылку на рег/логин, и показать user
        } else {
            log.debug("user[{}] goes on", user);
            chain.doFilter(request, response);
        }
    }

    private void notLogged(HttpServletRequest request, HttpServletResponse response, FilterChain chain, boolean loginRequest) throws ServletException, IOException {
        if (loginRequest) {
            log.debug("guest want to login");
            chain.doFilter(request, response);
        } else {
            log.debug("guest does not have access, redirect to index.html");
            response.sendRedirect("index.html");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }


    private void reconnect() throws SQLException {
    }
}