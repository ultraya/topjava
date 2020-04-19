package ru.javawebinar.topjava.service

import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import ru.javawebinar.topjava.*
import java.time.LocalDate
import java.time.Month

abstract class MealServiceTest : AbstractServiceTest() {

    @Autowired
    protected lateinit var mealService: MealService

    companion object {

        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)

        const val NOT_EXISTS_MEAL_ID = 10
    }

    @Test
    fun create() {
        val newMeal = getCreated()
        with(mealService) {
            newMeal.id = create(USER_ID, newMeal)?.id
            assertMatch(getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1)
        }
    }

    @Test
    fun update() {
        val updatedMeal = getUpdated()
        with(mealService) {
            update(USER_ID, updatedMeal)
            assertMatch(get(USER_ID, MEAL_ID1), updatedMeal)
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
            assertMatch(getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2)
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
            assertMatch(meal, MEAL1)
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
        assertMatch(
                mealService.getAll(USER_ID),
                MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1
        )
    }

    @Test
    fun getBetweenDate() {
        assertMatch(
                mealService.getFilteredByDate(
                        USER_ID,
                        LocalDate.of(2015, Month.MAY, 31),
                        LocalDate.of(2015, Month.MAY, 31)
                ),
                MEAL6, MEAL5, MEAL4
        )
    }
}