package ru.javawebinar.topjava.repository.inmemory

import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.model.User
import ru.javawebinar.topjava.repository.UserRepository
import ru.javawebinar.topjava.util.UsersUtil
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Repository
class InMemoryUserRepository : UserRepository {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")  //In fact, IntelliJ IDEA marks the declaration of the logger with a warning, because it recognizes that the reference to javaClass in a companion object probably isn't what we want.
        @JvmStatic  //converts companion objects to static fields in JVM
        private val log = getLogger(javaClass.enclosingClass) //enclosingClass refers to the outer class
    }

    private val repo: MutableMap<Int, User> = ConcurrentHashMap()
    private val key = AtomicInteger(0)

    init {
        save(UsersUtil.USER_1)
        save(UsersUtil.USER_2)
        save(UsersUtil.ADMIN)
    }

    override fun delete(id: Int): Boolean {
        log.info("delete {}", id)
        return repo.remove(id) != null
    }

    override fun save(user: User): User? {
        log.info("save {}", user)
        if (user.isNew) {
            user.id = key.incrementAndGet()
            repo.put(user.id, user)
            return user
        }

        return repo.computeIfPresent(user.id) { _, _ -> user }
    }

    override fun get(id: Int): User? {
        log.info("get {}", id)
        return repo[id]
    }

    override fun getAll(): List<User> = repo.run {
        log.info("getAll")
        values.sortedWith(compareBy({ it.name }, {it.email}))
    }

    override fun getByEmail(email: String): User? {
        log.info("getByEmail {}", email)
        return repo.values.firstOrNull {
            it.email == email
        }
    }
}
