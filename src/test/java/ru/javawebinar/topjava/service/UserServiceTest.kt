package ru.javawebinar.topjava.service

import org.junit.Assume
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import ru.javawebinar.topjava.ADMIN
import ru.javawebinar.topjava.USER
import ru.javawebinar.topjava.USER_ID
import ru.javawebinar.topjava.USER_MATCHER
import ru.javawebinar.topjava.model.Role
import ru.javawebinar.topjava.model.User
import ru.javawebinar.topjava.repository.JpaUtil
import ru.javawebinar.topjava.util.exception.NotFoundException
import java.util.*
import javax.validation.ConstraintViolationException

abstract class UserServiceTest : AbstractServiceTest() {

    @Autowired
    protected lateinit var service: UserService

    @Autowired
    private lateinit var jpaUtil: JpaUtil

    @Before
    fun setup() {
        cacheManager.getCache("users")?.clear()
        cacheManager.getCache("user")?.clear()
        jpaUtil.clear2ndLevelHibernateCache()
    }

    @Test
    fun create() {
        val newUser = User(null, "New", "new@gmail.com", "newPass", 1555, false, Date(), mutableSetOf(Role.ROLE_USER))
        val created = service.create(newUser)
        newUser.id = created.id
        USER_MATCHER.assertMatch(service.all, ADMIN, newUser, USER)
    }

    //@Rollback(false)
    @Test(expected = DataAccessException::class)
    fun duplicateMailCreate() {
        service.create(User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER))
    }

    @Test
    fun delete() {
        service.delete(USER_ID)
        USER_MATCHER.assertMatch(service.all, ADMIN)
    }

    @Test(expected = NotFoundException::class)
    fun deletedNotFound() {
        service.delete(1)
    }

    @Test
    fun get() {
        val user = service[USER_ID]
        USER_MATCHER.assertMatch(user, USER)
    }

    @Test(expected = NotFoundException::class)
    fun getNotFound() {
        service[1]
    }

    @Test
    fun getByEmail() {
        val user = service.getByEmail("user@yandex.ru");
        USER_MATCHER.assertMatch(user, USER);
    }

    @Test
    fun update() {
        val updated = User(USER).apply {
            name = "UpdatedName"
            caloriesPerDay = 330
        }
        service.update(updated);
        USER_MATCHER.assertMatch(service[USER_ID], updated)
    }

    @Test
    fun getAll() {
        USER_MATCHER.assertMatch(service.all, ADMIN, USER);
    }

    @Test
    fun createWithException() {
        Assume.assumeFalse(env.activeProfiles.contains("jdbc"))

        with(service) {
            validateRootCase(ConstraintViolationException::class.java) {
                create(User(null, "    ", "new@gmail.com", "newPass", 1555, false, Date(), mutableSetOf(Role.ROLE_USER)))
            }
            validateRootCase(ConstraintViolationException::class.java) {
                create(User(null, "New", "new@gmail", "newPass", 1555, false, Date(), mutableSetOf(Role.ROLE_USER)))
            }
            validateRootCase(ConstraintViolationException::class.java) {
                create(User(null, "New", "new@gmail.com", "newP", 1555, false, Date(), mutableSetOf(Role.ROLE_USER)))
            }
            validateRootCase(ConstraintViolationException::class.java) {
                create(User(null, "New", "new@gmail.com", "newPass", 5, false, Date(), mutableSetOf(Role.ROLE_USER)))
            }
            validateRootCase(ConstraintViolationException::class.java) {
                create(User(null, "New", "new@gmail.com", "newPass", 1555, null, Date(), mutableSetOf(Role.ROLE_USER)))
            }
            validateRootCase(ConstraintViolationException::class.java) {
                create(User(null, "New", "new@gmail.com", "newPass", 1555, true, null, mutableSetOf(Role.ROLE_USER)))
            }
            validateRootCase(ConstraintViolationException::class.java) {
                create(User(null, "New", "new@gmail.com", "newPass", 1555, true, null, mutableSetOf(Role.ROLE_USER)))
            }
        }
    }
}