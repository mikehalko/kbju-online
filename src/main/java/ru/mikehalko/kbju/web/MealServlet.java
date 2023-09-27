package ru.mikehalko.kbju.web;


import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.meal.Nutritionally;
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
import ru.mikehalko.kbju.util.web.validation.MealValidation;

import java.io.IOException;
import java.util.List;

import static ru.mikehalko.kbju.util.web.RequestParser.parseInt;
import static ru.mikehalko.kbju.util.web.RequestParser.parseMealValid;
import static ru.mikehalko.kbju.web.constant.MealParams.*;

public class MealServlet extends HttpServlet {
    public static final String ATTRIBUTE_USER = "user";
    public static final String ATTRIBUTE_MEAL = "meal";
    public static final String ATTRIBUTE_MEALS_LIST = "list";
    public static final String ATTRIBUTE_VALIDATION = "validator";

    public static final String PARAM_ACTION = "action";
    public static final String PARAM_ACTION_GET = "get";
    public static final String PARAM_ACTION_UPDATE = "update";
    public static final String PARAM_ACTION_CREATE = "create";
    public static final String PARAM_ACTION_DELETE = "delete";

    public static final String GET_FORWARD_SHOW = "views/meals/show.jsp";
    public static final String GET_FORWARD_UPDATE = "views/meals/meal-form.jsp";
    public static final String GET_FORWARD_CREATE = "views/meals/meal-form.jsp";
    public static final String GET_FORWARD_GET_ALL = "views/meals/meals.jsp";
    public static final String GET_REDIRECT_AFTER_DELETE = "meals";
    public static final String POST_REDIRECT_AFTER_CREATE_MEAL_ACTION_GET_ID = "meals?action=get&id=";

    public static final MealTo mockMealTo = new MealTo(new Nutritionally()); // TODO mock


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

        String action = request.getParameter(PARAM_ACTION);
        if (action == null) action = "";
        int id;
        MealTo meal;
        switch (action) {
            case PARAM_ACTION_GET:
                id = parseInt(request, PARAM_ID);
                log.debug("get id={}", id);
                meal = MealsUtil.getTo(repository.get(id, ServletSecurityUtil.authId(request)));
                request.setAttribute(ATTRIBUTE_MEAL, meal);
                request.getRequestDispatcher(GET_FORWARD_SHOW).forward(request, response);
                break;
            case PARAM_ACTION_UPDATE:
                id = parseInt(request, PARAM_ID);
                log.debug("prepare update id={}", id);
                meal = MealsUtil.getTo(repository.get(id, ServletSecurityUtil.authId(request)));
                // TODO или здесь надо обнулить атрибут валидации
                request.setAttribute(ATTRIBUTE_VALIDATION, null);
                request.setAttribute(ATTRIBUTE_MEAL, meal);
                request.getRequestDispatcher(GET_FORWARD_UPDATE).forward(request, response);
                break;
            case PARAM_ACTION_CREATE:
                log.debug("create forward");
                request.setAttribute(ATTRIBUTE_MEAL, new MealTo());
                request.getRequestDispatcher(GET_FORWARD_CREATE).forward(request, response);
                break;
            case PARAM_ACTION_DELETE:
                id = parseInt(request, PARAM_ID);
                log.debug("delete id={}", id);
                repository.delete(id, ServletSecurityUtil.authId(request));
                response.sendRedirect(GET_REDIRECT_AFTER_DELETE);
                break;
            default:
                log.debug("get-all (default)");
                List<MealTo> result =
                        MealsUtil.getTos(repository.getAll(ServletSecurityUtil.authId(request)),
                                ServletSecurityUtil.caloriesMin(request), ServletSecurityUtil.caloriesMax(request));
                log.debug("meals transfer = {}", result);
                request.setAttribute(ATTRIBUTE_MEALS_LIST, result);
                request.getRequestDispatcher(GET_FORWARD_GET_ALL).forward(request, response);
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        log.debug("doPost");
        MealValidation validation = new MealValidation();
        Meal meal = parseMealValid(request, ServletSecurityUtil.getUserSession(request), validation);
        MealTo mealTo = meal != null ? MealsUtil.getTo(meal) : mockMealTo;

        if (validation.isNotValid()) {
            failPost(validation, request, response, mealTo);
            return;
        }

        // TODO обнулить атрибут валидации
        request.setAttribute(ATTRIBUTE_VALIDATION, null);

        if (meal == null) {
            log.error("meal is NULL!");
            throw new RuntimeException("post: meal is null");
        }

        log.debug(meal.isNew() ? "save {}" : "create {}", meal);
        repository.save(meal, ServletSecurityUtil.authId(request));

        response.sendRedirect(POST_REDIRECT_AFTER_CREATE_MEAL_ACTION_GET_ID + meal.getId());
    }

    private void failPost(MealValidation validation, HttpServletRequest request, HttpServletResponse response, MealTo meal) throws ServletException, IOException {
        String message = validation.resultMessage();
        log.debug("invalid data form = {}", message);
        log.debug("set meal form = {}", meal); // TODO убрать
        request.setAttribute(ATTRIBUTE_VALIDATION, validation);
        request.setAttribute(ATTRIBUTE_MEAL, meal);

        String action = request.getParameter(PARAM_ACTION);
        log.debug("fail message = {}", message);
        log.debug("valid fields = id:{}, dt:{}, d:{}, m:{}, c:{}, p:{}, f:{}, ch:{}",
                validation.isValid(PARAM_ID),
                validation.isValid(PARAM_DATE_TIME),
                validation.isValid(PARAM_DESCRIPTION),
                validation.isValid(PARAM_MASS),
                validation.isValid(PARAM_CALORIES),
                validation.isValid(PARAM_PROTEINS),
                validation.isValid(PARAM_FATS),
                validation.isValid(PARAM_CARBOHYDRATES)
                ); // TODO убрать !!!

        switch (action) {
            case PARAM_ACTION_UPDATE:
                log.debug("create forward after fail");
                request.getRequestDispatcher(GET_FORWARD_UPDATE).forward(request, response);
                break;
            case PARAM_ACTION_CREATE:
                log.debug("update forward after fail");
                request.getRequestDispatcher(GET_FORWARD_CREATE).forward(request, response);
                break;
        }
    }
}