package ru.javawebinar.topjava.web.user

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import ru.javawebinar.topjava.UserTestData
import ru.javawebinar.topjava.UserTestData.ADMIN
import ru.javawebinar.topjava.model.User
import ru.javawebinar.topjava.repository.mock.InMemoryUserRepository
import ru.javawebinar.topjava.util.exception.NotFoundException

@RunWith(SpringRunner::class)
@ContextConfiguration("classpath:spring/spring-app.xml")
class InMemoryAdminRestControllerTest {
    @Autowired
    private lateinit var controller: AdminRestController
    @Autowired
    private lateinit var repository: InMemoryUserRepository

    @Before
    @Throws(Exception::class)
    fun setUp() {
        repository.init()
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        controller.delete(UserTestData.USER_ID)
        val users = controller.all
        Assert.assertEquals(users.size.toLong(), 1)
        Assert.assertEquals(users.iterator().next(), ADMIN)
    }

    @Test(expected = NotFoundException::class)
    @Throws(Exception::class)
    fun deleteNotFound() {
        controller.delete(10)
    }
}