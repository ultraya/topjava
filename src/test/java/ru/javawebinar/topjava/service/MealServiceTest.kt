package ru.javawebinar.topjava.service

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.rules.Stopwatch
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
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
import java.util.concurrent.TimeUnit

//https://spring.io/blog/2011/06/21/spring-3-1-m2-testing-with-configuration-classes-and-profiles
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [CoreConfig::class, DaoConfig::class])
@Sql(value = ["classpath:db/populateDB.sql"], config = SqlConfig(encoding = "UTF-8"))
@ActiveProfiles("hsqldb")
class MealServiceTest {

    @Autowired
    private lateinit var mealService: MealService

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    @Rule
    @JvmField
    val watcher = object : TestWatcher() {

        var start: Long = 0

        override fun starting(description: Description?) {
            start = System.nanoTime()
        }

        override fun finished(description: Description?) {
            infoDuration[description!!.methodName] = System.nanoTime() - start
        }
    }

    @Rule
    @JvmField
    val stopwatch = object : Stopwatch() {
        override fun finished(nanos: Long, description: Description?) {
            val s = String.format("\n%-25s %7d", description!!.methodName, TimeUnit.NANOSECONDS.toMillis(nanos))
            result.append(s)
            log.info(s)
        }
    }

    companion object {

        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)

        const val NOT_EXISTS_MEAL_ID = 10

        val infoDuration = mutableMapOf<String, Long>()

        val result = StringBuilder()

        @BeforeClass
        @JvmStatic
        fun startTest() = println("TEST EXECUTING...")

        @AfterClass
        @JvmStatic
        fun endTest() {
            infoDuration.map {
                "${it.key}: ${TimeUnit.NANOSECONDS.toMillis(it.value)}"
            }.forEach {
                log.info(it)
            }
        }
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

    private fun expectedNotFoundException(msg: String) {
        expectedException.expect(NotFoundException::class.java)
        expectedException.expectMessage(msg)
    }
}