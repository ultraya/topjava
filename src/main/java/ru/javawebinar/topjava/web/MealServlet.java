package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MockMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class MealServlet extends HttpServlet {

    private final MealRepository mealRepository = new MockMealRepository();

    private static final Logger log = getLogger(MealServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal editMeal = mealFromRequest(request);
        mealRepository.save(editMeal);
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String paramId = request.getParameter("id");
        String action = request.getParameter("action");

        List<MealTo> meals = MealsUtil.getFiltered(mealRepository.getAll(), LocalTime.MIN, LocalTime.MAX, DEFAULT_CALORIES_PER_DAY);

        if (action == null || action.isEmpty()) {
            request.setAttribute("meals", meals);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {

            if (paramId == null || paramId.isEmpty()) {
                throw new IllegalArgumentException("id must not be null or empty");
            }

            int id = Integer.parseInt(paramId);

            switch (action) {
                case "delete":
                    mealRepository.delete(id);
                    response.sendRedirect("meals");
                    break;
                case "edit":
                    Meal meal = mealRepository.get(id);
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
                    return;
                default:
                    throw new IllegalArgumentException("action is not applicable");
            }
        }
    }

    private Meal mealFromRequest(HttpServletRequest req) {
        return new Meal(
                req.getParameter("id") == null ? null : Integer.parseInt(req.getParameter("id")),
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories"))
        );
    }
}
