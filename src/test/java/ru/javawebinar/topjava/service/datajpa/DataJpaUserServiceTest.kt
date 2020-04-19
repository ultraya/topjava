package ru.javawebinar.topjava.service.datajpa

import org.junit.Test
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import ru.javawebinar.topjava.*
import ru.javawebinar.topjava.service.UserServiceTest

@ActiveProfiles(value = [Profiles.DATAJPA])
@Transactional  // user has lazy field and when test fail and print info call toString that call lazy field
class DataJpaUserServiceTest : UserServiceTest() {

    @Test
    fun getWithMeals() {
        val user = service.getWithMeals(USER_ID)
        assertMatch(user, USER)
        assertMatch(user.meals, USER_MEALS)
    }
}