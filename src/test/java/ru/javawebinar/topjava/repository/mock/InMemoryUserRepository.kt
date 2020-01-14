package ru.javawebinar.topjava.repository.mock

import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.ADMIN
import ru.javawebinar.topjava.ADMIN_ID
import ru.javawebinar.topjava.USER
import ru.javawebinar.topjava.USER_ID
import ru.javawebinar.topjava.model.User
import ru.javawebinar.topjava.repository.UserRepository

@Repository("mockUserRepository")
class InMemoryUserRepository : InMemoryBaseRepository<User>(), UserRepository {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")  //In fact, IntelliJ IDEA marks the declaration of the logger with a warning, because it recognizes that the reference to javaClass in a companion object probably isn't what we want.
        @JvmStatic  //converts companion objects to static fields in JVM
        private val log = getLogger(javaClass.enclosingClass) //enclosingClass refers to the outer class
    }

    fun init() {
        map.clear()
        map[USER_ID] = USER
        map[ADMIN_ID] = ADMIN
    }

    override fun getAll(): List<User> =
            getCollection().sortedWith(
                    compareBy(
                            { it.name },
                            { it.email }
                    ))

    override fun getByEmail(email: String): User? =
            getCollection().firstOrNull {
                it.email == email
            }

}
