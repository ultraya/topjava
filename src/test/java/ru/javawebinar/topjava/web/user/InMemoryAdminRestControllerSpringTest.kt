package ru.javawebinar.topjava.web.user

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import ru.javawebinar.topjava.ADMIN
import ru.javawebinar.topjava.USER_ID
import ru.javawebinar.topjava.config.CoreConfig
import ru.javawebinar.topjava.config.MockConfig
import ru.javawebinar.topjava.repository.mock.InMemoryUserRepository
import ru.javawebinar.topjava.util.exception.NotFoundException

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [CoreConfig::class, MockConfig::class])  //https://spring.io/blog/2011/06/21/spring-3-1-m2-testing-with-configuration-classes-and-profiles
class InMemoryAdminRestControllerSpringTest {

    @Autowired
    private lateinit var controller: AdminRestController

    @Autowired
    @Qualifier("mockUserRepository")
    private lateinit var repository: InMemoryUserRepository

    @Before
    @Throws(Exception::class)
    fun setUp() {
        repository.init()
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        controller.delete(USER_ID)
        val users = controller.all
        assertEquals(users.size, 1)
        assertEquals(users.iterator().next(), ADMIN)
    }

    @Test(expected = NotFoundException::class)
    @Throws(Exception::class)
    fun deleteNotFound() {
        controller.delete(10)
    }
}