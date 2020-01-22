package ru.javawebinar.topjava.service

import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.bridge.SLF4JBridgeHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.junit4.SpringRunner
import ru.javawebinar.topjava.*
import ru.javawebinar.topjava.config.CoreConfig
import ru.javawebinar.topjava.config.DaoConfig
import ru.javawebinar.topjava.util.exception.NotFoundException
import java.time.LocalDate
import java.time.Month

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [CoreConfig::class, DaoConfig::class])
@Sql(value = ["classpath:db/initDB.sql", "classpath:db/populateDB.sql"], config = SqlConfig(encoding = "UTF-8"))
class MealServiceTest {

    @Autowired
    private lateinit var mealService: MealService

    companion object {
        init {
            // let java.util.logging log to slf4j
            SLF4JBridgeHandler.removeHandlersForRootLogger()
            SLF4JBridgeHandler.install()
        }

        val NOT_EXISTS_MEAL_ID = 10
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

    @Test(expected = NotFoundException::class)
    fun updateNotFound() {
        with(mealService) {
            update(USER_ID, getUpdated().apply { id = NOT_EXISTS_MEAL_ID })
        }
    }

    @Test(expected = NotFoundException::class)
    fun updateNotOwn() {
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

    @Test(expected = NotFoundException::class)
    fun deleteNotFound() {
        mealService.delete(USER_ID, NOT_EXISTS_MEAL_ID)
    }

    @Test(expected = NotFoundException::class)
    fun deleteNotOwn() {
        mealService.delete(ADMIN_ID, MEAL_ID1)
    }

    @Test
    fun get() {
        with(mealService) {
            val meal = get(USER_ID, MEAL_ID1)
            assertMatch(meal, MEAL1)
        }
    }

    @Test(expected = NotFoundException::class)
    fun getNotFound() {
        mealService.get(USER_ID, NOT_EXISTS_MEAL_ID)
    }

    @Test(expected = NotFoundException::class)
    fun getNotOwn() {
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