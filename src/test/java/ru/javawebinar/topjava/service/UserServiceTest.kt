package ru.javawebinar.topjava.service

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import ru.javawebinar.topjava.ADMIN
import ru.javawebinar.topjava.USER
import ru.javawebinar.topjava.USER_ID
import ru.javawebinar.topjava.assertMatch
import ru.javawebinar.topjava.config.CoreConfig
import ru.javawebinar.topjava.config.DaoConfig
import ru.javawebinar.topjava.model.Role
import ru.javawebinar.topjava.model.User
import ru.javawebinar.topjava.util.exception.NotFoundException
import java.util.*

@ContextConfiguration(classes = [CoreConfig::class, DaoConfig::class])
@RunWith(SpringRunner::class)
@Sql(scripts = ["classpath:db/populateDB.sql"], config = SqlConfig(encoding = "UTF-8"))
class UserServiceTest {
    //    static {
    //        // Only for postgres driver logging
    //        // It uses java.util.logging and logged via jul-to-slf4j bridge
    //        SLF4JBridgeHandler.install();
    //    }
    @Autowired
    private lateinit var service: UserService

    @Test
    fun create() {
        val newUser = User(null, "New", "new@gmail.com", "newPass", 1555, false, Date(), mutableSetOf(Role.ROLE_USER))
        val created = service.create(newUser)
        newUser.id = created.id
        assertMatch(service.all, ADMIN, newUser, USER)
    }

    @Test(expected = DataAccessException::class)
    fun duplicateMailCreate() {
        service.create(User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER))
    }

    @Test
    fun delete() {
        service.delete(USER_ID)
        assertMatch(service.all, ADMIN)
    }

    @Test(expected = NotFoundException::class)
    fun deletedNotFound() {
        service.delete(1)
    }

    @Test
    fun get() {
        val user = service[USER_ID]
        assertMatch(user, USER)
    }

    @Test(expected = NotFoundException::class)
    fun getNotFound() {
        service[1]
    }

    @Test
    fun getByEmail() {
        val user = service.getByEmail("user@yandex.ru");
        assertMatch(user, USER);
    }

    @Test
    fun update() {
        val updated = User(USER).apply {
            name = "UpdatedName"
            caloriesPerDay = 330
        }
        service.update(updated);
        assertMatch(service[USER_ID], updated)
    }

    @Test
    fun getAll() {
        assertMatch(service.all, ADMIN, USER);
    }
}