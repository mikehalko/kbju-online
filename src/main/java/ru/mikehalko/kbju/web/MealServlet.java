package ru.mikehalko.kbju.web;


import ru.mikehalko.kbju.model.Meal;
import ru.mikehalko.kbju.model.Nutritionally;
import ru.mikehalko.kbju.model.User;
import ru.mikehalko.kbju.repository.fake.FakeMealRepository;
import ru.mikehalko.kbju.service.MealService;
import ru.mikehalko.kbju.to.MealTo;
import ru.mikehalko.kbju.util.MealsUtil;
import ru.mikehalko.kbju.util.SecurityUtil;
import ru.mikehalko.kbju.util.fake.FakeBuilder;
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


public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private static final FakeManager fakeManage =
            new FakeManager(new FakeMealRepository(new FakeBuilder()));

    static MealService service = new MealService(fakeManage.getRepository());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet");
        request.setAttribute("user", SecurityUtil.user());
        String action = request.getParameter("action");
        if (action == null) action = "";
        int id;
        MealTo meal;
        switch (action) {
            case "get":
                log.debug("case get");
                id = Integer.parseInt(request.getParameter("id"));
                meal = MealsUtil.getTo(service.get(id, SecurityUtil.authId()));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("views/show.jsp").forward(request, response);
                break;
            case "update":
                log.debug("case update");
                id = Integer.parseInt(request.getParameter("id"));
                meal = MealsUtil.getTo(service.get(id, SecurityUtil.authId()));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("views/update.jsp").forward(request, response);
                break;
            case "create":
                log.debug("case create");
                request.getRequestDispatcher("views/create.jsp").forward(request, response);
                break;
            case "delete":
                log.debug("case delete");
                id = Integer.parseInt(request.getParameter("id"));
                service.delete(id, SecurityUtil.authId());
            default:
                log.debug("case get-all (default)");
                List<MealTo> result =
                        MealsUtil.getTos(service.getAll(SecurityUtil.authId()), SecurityUtil.nutritionalValue());
                request.setAttribute("list", result);
                request.getRequestDispatcher("views/meals.jsp").forward(request, response);
                break;
        }

    }

    private static int ids = 1000;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost");
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int mass = Integer.parseInt(request.getParameter("mass"));
        int proteins = Integer.parseInt(request.getParameter("proteins"));
        int fats = Integer.parseInt(request.getParameter("fats"));
        int carbohydrates = Integer.parseInt(request.getParameter("carbohydrates"));
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal meal = createMeal(id.isEmpty() ? ids++ : Integer.parseInt(id), SecurityUtil.user(),
                dateTime, mass, description, proteins, fats, carbohydrates, calories);

        if (id.isEmpty()) {
            log.debug("update {}", meal);
            service.update(meal, SecurityUtil.authId());
        } else {
            log.debug("create {}", meal);
            service.create(meal, SecurityUtil.authId());
        }

        response.sendRedirect("meals?action=get&id=" + meal.getId());
    }

    private static Meal createMeal(int id, User user, LocalDateTime dateTime, int mass, String description,
                                   int proteins, int fats, int carbohydrates, int calories) {
        if (calories == 0) calories = MealsUtil.calculateCalories(proteins, fats, carbohydrates);
        Nutritionally nutritionally = new Nutritionally(proteins, fats, carbohydrates, calories);
        return new Meal(id, user, dateTime, mass, description, nutritionally);
    }
}