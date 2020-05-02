package ru.javawebinar.topjava.service

import org.junit.Assume
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.javawebinar.topjava.*
import ru.javawebinar.topjava.model.Meal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import javax.validation.ConstraintViolationException


abstract class MealServiceTest : AbstractServiceTest() {

    @Autowired
    protected lateinit var mealService: MealService

    companion object {
        const val NOT_EXISTS_MEAL_ID = 10
    }

    @Test
    fun create() {
        val newMeal = getCreated()
        with(mealService) {
            newMeal.id = create(USER_ID, newMeal)?.id
            MEAL_MATCHER.assertMatch(getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1)
        }
    }

    @Test
    fun update() {
        val updatedMeal = getUpdated()
        with(mealService) {
            update(USER_ID, updatedMeal)
            MEAL_MATCHER.assertMatch(get(USER_ID, MEAL_ID1), updatedMeal)
        }
    }

    @Test
    fun updateNotFound() {
        expectedNotFoundException("Not found entity with id=$NOT_EXISTS_MEAL_ID")
        with(mealService) {
            update(USER_ID, getUpdated().apply { id = NOT_EXISTS_MEAL_ID })
        }
    }

    @Test
    fun updateNotOwn() {
        expectedNotFoundException("Not found entity with id=$MEAL_ID1")
        with(mealService) {
            update(ADMIN_ID, getUpdated())
        }
    }

    @Test
    fun delete() {
        with(mealService) {
            delete(USER_ID, MEAL_ID1)
            MEAL_MATCHER.assertMatch(getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2)
        }
    }

    @Test
    fun deleteNotFound() {
        expectedNotFoundException("Not found entity with id=$NOT_EXISTS_MEAL_ID")
        mealService.delete(USER_ID, NOT_EXISTS_MEAL_ID)
    }

    @Test
    fun deleteNotOwn() {
        expectedNotFoundException("Not found entity with id=$MEAL_ID1")
        mealService.delete(ADMIN_ID, MEAL_ID1)
    }

    @Test
    fun get() {
        with(mealService) {
            val meal = get(USER_ID, MEAL_ID1)
            MEAL_MATCHER.assertMatch(meal, MEAL1)
        }
    }

    @Test
    fun getNotFound() {
        expectedNotFoundException("Not found entity with id=$NOT_EXISTS_MEAL_ID")
        mealService.get(USER_ID, NOT_EXISTS_MEAL_ID)
    }

    @Test
    fun getNotOwn() {
        expectedNotFoundException("Not found entity with id=$MEAL_ID1")
        mealService.get(ADMIN_ID, MEAL_ID1)
    }

    @Test
    fun getAll() {
        MEAL_MATCHER.assertMatch(
                mealService.getAll(USER_ID),
                MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1
        )
    }

    @Test
    fun getBetweenDate() {
        MEAL_MATCHER.assertMatch(
                mealService.getFilteredByDate(
                        USER_ID,
                        LocalDate.of(2015, Month.MAY, 31),
                        LocalDate.of(2015, Month.MAY, 31)
                ),
                MEAL6, MEAL5, MEAL4
        )
    }

    @Test
    fun createWithException() {
        Assume.assumeFalse(env.activeProfiles.contains("jdbc"))

        with(mealService) {
            validateRootCase(ConstraintViolationException::class.java) {
                create(USER_ID, Meal(null, LocalDateTime.now(), "  ", 300))
            }
            validateRootCase(ConstraintViolationException::class.java) {
                create(USER_ID, Meal(null, LocalDateTime.now(), "Description", 9))
            }
            validateRootCase(ConstraintViolationException::class.java) {
                create(USER_ID, Meal(null, LocalDateTime.now(), "Description", 5001))
            }
        }

    }
}