package ru.mikehalko.kbju.web;


import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.mock.MealToMock;
import ru.mikehalko.kbju.repository.MealRepository;
import ru.mikehalko.kbju.repository.sql.MealRepositorySQL;
import ru.mikehalko.kbju.to.MealTo;
import ru.mikehalko.kbju.util.model.MealsUtil;
import ru.mikehalko.kbju.util.security.ServletSecurityUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mikehalko.kbju.util.web.RequestParser;
import ru.mikehalko.kbju.util.web.validation.MealValidator;
import ru.mikehalko.kbju.web.constant.parameter.Parameter;

import java.io.IOException;
import java.util.List;

import static ru.mikehalko.kbju.util.web.RequestParser.parseInt;
import static ru.mikehalko.kbju.web.constant.attribute.MealField.*;
import static ru.mikehalko.kbju.web.constant.OtherConstant.*;
import static ru.mikehalko.kbju.web.constant.parameter.Parameter.*;
import static ru.mikehalko.kbju.util.web.Util.*;

public class MealServlet extends HttpServlet {
    public static final String GET_FORWARD_SHOW = "views/meals/show.jsp";
    public static final String GET_FORWARD_UPDATE = "views/meals/meal-form.jsp";
    public static final String GET_FORWARD_CREATE = "views/meals/meal-form.jsp";
    public static final String GET_FORWARD_GET_ALL = "views/meals/meals.jsp";
    public static final String POST_REDIRECT_AFTER_CREATE_MEAL_ACTION_GET_ID = SERVLET_MEAL + "?" + ACTION + "=" + ACTION_GET + "&id=";
    public static final String GET_REDIRECT_AFTER_DELETE = SERVLET_MEAL.value();

    public static final MealTo mockMealTo = new MealToMock();
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static MealRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        repository = MealRepositorySQL.getInstance();
        super.init(config);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet");

        String action = getParameter(request, ACTION);
        if (action == null) action = ACTION_GET_ALL.value();
        int id;
        MealTo meal;
        switch (Parameter.byValue(action)) {
            case ACTION_GET:
                id = parseInt(request, PARAM_ID);
                log.debug("get id={}", id);
                meal = MealsUtil.getTo(repository.get(id, ServletSecurityUtil.authId(request)));
                setAttribute(request, MEAL, meal);
                forward(request, response, GET_FORWARD_SHOW);
                break;
            case ACTION_UPDATE:
                id = parseInt(request, PARAM_ID);
                log.debug("update forward id={}", id);
                meal = MealsUtil.getTo(repository.get(id, ServletSecurityUtil.authId(request)));
                request.setAttribute(MEAL.value(), meal);
                setAttribute(request, MEAL, meal);
                forward(request, response, GET_FORWARD_UPDATE);
                break;
            case ACTION_CREATE:
                log.debug("create forward");
                setAttribute(request, MEAL, mockMealTo);
                forward(request, response, GET_FORWARD_CREATE);
                break;
            case ACTION_DELETE:
                log.debug("delete");
                id = parseInt(request, PARAM_ID);
                log.debug("delete id={}", id);
                repository.delete(id, ServletSecurityUtil.authId(request));
                response.sendRedirect(GET_REDIRECT_AFTER_DELETE);
                break;
            case ACTION_GET_ALL:
                log.debug("get-all");
                List<MealTo> result = MealsUtil.getTos(repository.getAll(ServletSecurityUtil.authId(request)),
                        ServletSecurityUtil.caloriesMin(request), ServletSecurityUtil.caloriesMax(request));
                setAttribute(request, ATTRIBUTE_MEALS_LIST, result);
                forward(request, response, GET_FORWARD_GET_ALL);
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        log.debug("doPost");
        MealValidator validation = new MealValidator();
        Meal meal = RequestParser.meal(request, ServletSecurityUtil.getUserSession(request), validation);
        MealTo mealTo = meal != null ? MealsUtil.getTo(meal) : mockMealTo;

        if (validation.isNotValid()) {
            failPost(validation, request, response, mealTo);
            return;
        }

        if (meal == null) {
            log.error("meal is NULL!");
            throw new RuntimeException("post: meal is null");
        }

        log.debug(meal.isNew() ? "save {}" : "create {}", meal);
        repository.save(meal, ServletSecurityUtil.authId(request));

        response.sendRedirect(POST_REDIRECT_AFTER_CREATE_MEAL_ACTION_GET_ID + meal.getId());
    }

    private void failPost(MealValidator validation, HttpServletRequest request, HttpServletResponse response, MealTo meal) throws ServletException, IOException {
        String message = validation.resultMessage();
        log.debug("invalid data form = {}", message);
        setAttribute(request, VALIDATOR_MEAL, validation);
        setAttribute(request, MEAL, meal);

        String action = getParameter(request, ACTION);
        switch (Parameter.byValue(action)) {
            case ACTION_UPDATE:
                log.debug("create forward after fail");
                request.getRequestDispatcher(GET_FORWARD_UPDATE).forward(request, response);
                break;
            case ACTION_CREATE:
                log.debug("update forward after fail");
                request.getRequestDispatcher(GET_FORWARD_CREATE).forward(request, response);
                break;
        }
    }
}