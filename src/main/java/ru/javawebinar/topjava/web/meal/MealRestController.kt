package ru.javawebinar.topjava.web.meal

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import ru.javawebinar.topjava.model.Meal
import ru.javawebinar.topjava.service.MealService
import ru.javawebinar.topjava.to.MealTo
import ru.javawebinar.topjava.util.MealsUtil
import ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent
import ru.javawebinar.topjava.util.ValidationUtil.checkNew
import ru.javawebinar.topjava.web.SecurityUtil
import java.time.LocalDate
import java.time.LocalTime

@Controller
class MealRestController(
        private val service: MealService
) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")  //In fact, IntelliJ IDEA marks the declaration of the logger with a warning, because it recognizes that the reference to javaClass in a companion object probably isn't what we want.
        @JvmStatic  //converts companion objects to static fields in JVM
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass) //enclosingClass refers to the outer class
    }

    fun get(mealId: Int): Meal {
        log.info("delete")
        return service.get(SecurityUtil.authUserId(), mealId)
    }

    fun create(meal: Meal) {
        log.info("create")
        checkNew(meal)
        service.create(userId = SecurityUtil.authUserId(), meal = meal)
    }

    fun update(meal: Meal, mealId: Int) {
        log.info("update")
        assureIdConsistent(meal, mealId)
        service.update(userId = SecurityUtil.authUserId(), meal = meal)
    }

    fun delete(mealId: Int) {
        log.info("delete")
        service.delete(SecurityUtil.authUserId(), mealId)
    }

    fun getAll(): List<MealTo> {
        log.info("getAll")
        return MealsUtil.getTos(
                service.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay()
        )
    }

    fun getFiltered(startDate: LocalDate?, endDate: LocalDate?, startTime: LocalTime?, endTime: LocalTime?): List<MealTo> {
        log.info("getFiltered")
        return MealsUtil.getFilteredTos(
                service.getFilteredByDate(SecurityUtil.authUserId(), startDate, endDate),
                SecurityUtil.authUserCaloriesPerDay(),
                startTime ?: LocalTime.MIN,
                endTime ?: LocalTime.MAX
        )

    }
}