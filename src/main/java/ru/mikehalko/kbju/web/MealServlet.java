package ru.mikehalko.kbju.web;


import ru.mikehalko.kbju.model.meal.Meal;
import ru.mikehalko.kbju.model.user.User;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.mikehalko.kbju.util.model.MealsUtil.createMeal;

public class MealServlet extends HttpServlet {
    public static final String ATTRIBUTE_USER = "user";
    public static final String ATTRIBUTE_MEAL = "meal";
    public static final String ATTRIBUTE_MEALS_LIST = "list";


    public static final String PARAM_ACTION = "action";
    public static final String PARAM_ACTION_GET = "get";
    public static final String PARAM_ACTION_UPDATE = "update";
    public static final String PARAM_ACTION_CREATE = "create";
    public static final String PARAM_ACTION_DELETE = "delete";

    public static final String PARAM_ID = "id";
    public static final String PARAM_DATE_TIME = "dateTime";
    public static final String PARAM_DESCRIPTION = "description";
    public static final String PARAM_MASS = "mass";
    public static final String PARAM_PROTEINS = "proteins";
    public static final String PARAM_FATS = "fats";
    public static final String PARAM_CARBOHYDRATES = "carbohydrates";
    public static final String PARAM_CALORIES = "calories";

    public static final String GET_FORWARD_SHOW = "views/meals/show.jsp";
    public static final String GET_FORWARD_UPDATE = "views/meals/meal-form.jsp";
    public static final String GET_FORWARD_CREATE = "views/meals/meal-form.jsp";
    public static final String GET_FORWARD_GET_ALL = "views/meals/meals.jsp";
    public static final String GET_REDIRECT_AFTER_DELETE = "meals";
    public static final String POST_REDIRECT_AFTER_CREATE_MEAL_ACTION_GET_ID = "meals?action=get&id=";


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
                id = parseIntSafe(request, PARAM_ID);
                log.debug("get id={}", id);
                meal = MealsUtil.getTo(repository.get(id, ServletSecurityUtil.authId(request)));
                request.setAttribute(ATTRIBUTE_MEAL, meal);
                request.getRequestDispatcher(GET_FORWARD_SHOW).forward(request, response);
                break;
            case PARAM_ACTION_UPDATE:
                id = parseIntSafe(request, PARAM_ID);
                log.debug("prepare update id={}", id);
                meal = MealsUtil.getTo(repository.get(id, ServletSecurityUtil.authId(request)));
                request.setAttribute(ATTRIBUTE_MEAL, meal);
                request.getRequestDispatcher(GET_FORWARD_UPDATE).forward(request, response);
                break;
            case PARAM_ACTION_CREATE:
                log.debug("create forward");
                request.setAttribute(ATTRIBUTE_MEAL, new MealTo());
                request.getRequestDispatcher(GET_FORWARD_CREATE).forward(request, response);
                break;
            case PARAM_ACTION_DELETE:
                id = parseIntSafe(request, PARAM_ID);
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
        log.debug("doPost");
        request.setCharacterEncoding("UTF-8");

        Meal meal = parseMeal(request, ServletSecurityUtil.getUserSession(request));
        // TODO валидация

        log.debug(meal.isNew() ? "save {}" : "create {}", meal);
        repository.save(meal, ServletSecurityUtil.authId(request));

        response.sendRedirect(POST_REDIRECT_AFTER_CREATE_MEAL_ACTION_GET_ID + meal.getId());
    }




    public static int parseIntSafe(HttpServletRequest request, String param) {
        String id = Objects.requireNonNull(request.getParameter(param)); // TODO неверно!! NullPointer exception
        return id.isEmpty() ? 0 : Integer.parseInt(id); // TODO ноль должен и так приходить. Пусто быть не должно
    } // TODO убрать эти методы отсюда

    public static String parseStringSafe(HttpServletRequest request, String param) {
        return Objects.requireNonNull(request.getParameter(param));
    }

    public static LocalDateTime parseLocalDateTimeSafe(HttpServletRequest request, String param) {
        String dateTime = Objects.requireNonNull(request.getParameter(param));
        return dateTime.isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(dateTime); // not safe
    }

    // TODO убрать в util класс
    private static Meal parseMeal(HttpServletRequest request, User user) {
        int id = parseIntSafe(request, PARAM_ID);
        LocalDateTime dateTime = parseLocalDateTimeSafe(request ,PARAM_DATE_TIME);
        String description = parseStringSafe(request,PARAM_DESCRIPTION);
        int mass = parseIntSafe(request, PARAM_MASS);
        int proteins = parseIntSafe(request, PARAM_PROTEINS);
        int fats = parseIntSafe(request, PARAM_FATS);
        int carbohydrates = parseIntSafe(request, PARAM_CARBOHYDRATES);
        int calories = parseIntSafe(request, PARAM_CALORIES);

        return createMeal(id, user, dateTime, mass, description, proteins, fats, carbohydrates, calories);
    }
}