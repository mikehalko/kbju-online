package ru.mikehalko.kbju.web;


import ru.mikehalko.kbju.model.Meal;
import ru.mikehalko.kbju.model.Nutritionally;
import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.repository.MealRepository;
import ru.mikehalko.kbju.to.MealTo;
import ru.mikehalko.kbju.util.MealsUtil;
import ru.mikehalko.kbju.util.SecurityUtil;
import ru.mikehalko.kbju.util.fake.FakeManager;

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


public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private static MealRepository repository;

    private static final int DAYS = 3;
    private static final int MEALS_PER_DAY = 3;

    @Override
    public void init() throws ServletException {
        FakeManager fm = new FakeManager();
        SecurityUtil.setUser(fm.getFakeAuthUser());
        fm.initRepository();
        fm.setNumberOfDays(DAYS);
        fm.setMealsPerDay(MEALS_PER_DAY);
        repository = fm.getRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet");
        request.setAttribute("user", SecurityUtil.getUser());
        String action = request.getParameter("action");
        if (action == null) action = "";
        int id;
        MealTo meal;
        switch (action) {
            case "get":
                id = getIdSafe(request);
                log.debug("get id={}", id);
                meal = MealsUtil.getTo(repository.get(id, SecurityUtil.authId()));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("views/show.jsp").forward(request, response);
                break;
            case "update":
                id = getIdSafe(request);
                log.debug("prepare update id={}", id);
                meal = MealsUtil.getTo(repository.get(id, SecurityUtil.authId()));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("views/update.jsp").forward(request, response);
                break;
            case "create":
                log.debug("create forward");
                request.getRequestDispatcher("views/create.jsp").forward(request, response);
                break;
            case "delete":
                id = getIdSafe(request);
                log.debug("delete id={}", id);
                repository.delete(id, SecurityUtil.authId());
                response.sendRedirect("meals");
                break;
            default:
                log.debug("get-all (default)");
                List<MealTo> result =
                        MealsUtil.getTos(repository.getAll(SecurityUtil.authId()), SecurityUtil.nutritionalValue());
                request.setAttribute("list", result);
                request.getRequestDispatcher("views/meals.jsp").forward(request, response);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost");
        request.setCharacterEncoding("UTF-8");

        Meal meal = createMeal(request, SecurityUtil.getUser());

        log.debug(meal.isNew() ? "save {}" : "create {}", meal);
        repository.save(meal, SecurityUtil.authId());

        response.sendRedirect("meals?action=get&id=" + meal.getId());
    }

    public static int getIdSafe(HttpServletRequest request) {
        String id = Objects.requireNonNull(request.getParameter("id"));
        return id.isEmpty() ? 0 :  Integer.parseInt(id);
    }

    private static Meal createMeal(HttpServletRequest request, User user) {
        int id = getIdSafe(request);
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int mass = Integer.parseInt(request.getParameter("mass"));
        int proteins = Integer.parseInt(request.getParameter("proteins"));
        int fats = Integer.parseInt(request.getParameter("fats"));
        int carbohydrates = Integer.parseInt(request.getParameter("carbohydrates"));
        int calories = Integer.parseInt(request.getParameter("calories"));

        return createMeal(id, user, dateTime, mass, description, proteins, fats, carbohydrates, calories);
    }
    private static Meal createMeal(int id, User user, LocalDateTime dateTime, int mass, String description,
                                   int proteins, int fats, int carbohydrates, int calories) {
        if (calories == 0) calories = MealsUtil.calculateCalories(proteins, fats, carbohydrates);
        Nutritionally nutritionally = new Nutritionally(proteins, fats, carbohydrates, calories);
        return new Meal(id, user, dateTime, mass, description, nutritionally);
    }
}