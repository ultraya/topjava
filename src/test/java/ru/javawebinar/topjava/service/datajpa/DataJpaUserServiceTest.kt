package ru.javawebinar.topjava.service.datajpa

import org.junit.Assert.assertThrows
import org.junit.Test
import org.springframework.test.context.ActiveProfiles
import ru.javawebinar.topjava.*
import ru.javawebinar.topjava.service.UserServiceTest
import ru.javawebinar.topjava.util.exception.NotFoundException

@ActiveProfiles(value = [Profiles.DATAJPA])
//@Transactional  // user has lazy field and when test fail and print info call toString that call lazy field
class DataJpaUserServiceTest : UserServiceTest() {

    @Test
    fun getWithMeals() {
        val user = service.getWithMeals(USER_ID)
        USER_MATCHER.assertMatch(user, USER)
        MEAL_MATCHER.assertMatch(user.meals, USER_MEALS)
    }

    @Test
    fun getWithMealsNotFound() {
        assertThrows(NotFoundException::class.java) {
            service.getWithMeals(1)
        }
    }
}