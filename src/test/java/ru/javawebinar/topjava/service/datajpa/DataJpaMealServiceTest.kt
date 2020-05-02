package ru.javawebinar.topjava.service.datajpa

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import ru.javawebinar.topjava.*
import ru.javawebinar.topjava.service.MealServiceTest
import ru.javawebinar.topjava.util.exception.NotFoundException

@ActiveProfiles(value = [Profiles.DATAJPA])
class DataJpaMealServiceTest : MealServiceTest() {

    @Test
    fun getWithUser() {
        val meal = mealService.getWithUser(USER_ID, MEAL_ID1)
        MEAL_MATCHER.assertMatch(meal, MEAL1)
        USER_MATCHER.assertMatch(meal.user!!, USER)
    }

    @Test
    fun getWithUserNotFound() {
        assertThrows(NotFoundException::class.java) {
            mealService.getWithUser(1, MEAL_ID1)
        }
    }
}