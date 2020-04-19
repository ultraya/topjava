package ru.javawebinar.topjava.service.datajpa

import org.junit.Test
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import ru.javawebinar.topjava.*
import ru.javawebinar.topjava.service.MealServiceTest

@ActiveProfiles(value = [Profiles.DATAJPA])
@Transactional
class DataJpaMealServiceTest : MealServiceTest() {

    @Test
    fun getWithUser() {
        val meal = mealService.getWithUser(USER_ID, MEAL_ID1)
        assertMatch(meal, MEAL1)
        assertMatch(meal.user!!, USER)
    }
}