package ru.javawebinar.topjava.repository.inmemory

import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.model.User
import ru.javawebinar.topjava.repository.UserRepository
import ru.javawebinar.topjava.util.UsersUtil

@Repository
class InMemoryUserRepository : UserRepository {

    private val repo = InMemoryBaseRepository<User>()

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")  //In fact, IntelliJ IDEA marks the declaration of the logger with a warning, because it recognizes that the reference to javaClass in a companion object probably isn't what we want.
        @JvmStatic  //converts companion objects to static fields in JVM
        private val log = getLogger(javaClass.enclosingClass) //enclosingClass refers to the outer class
    }

    init {
        save(UsersUtil.USER_1)
        save(UsersUtil.USER_2)
        save(UsersUtil.ADMIN)
    }

    override fun save(user: User): User? {
        log.info("save {}", user)
        return repo.save(user)
    }

    override fun get(id: Int): User? {
        log.info("get {}", id)
        return repo.get(id)
    }

    override fun delete(id: Int): Boolean {
        log.info("delete {}", id)
        return repo.delete(id)
    }

    override fun getAll(): List<User> {
        log.info("getAll")
        return repo.getFiltered(
                { true },
                compareBy({ it.name }, { it.email })
        )
    }

    override fun getByEmail(email: String): User? {
        log.info("getByEmail {}", email)
        return repo.getFiltered {
            it.email == email
        }.firstOrNull()
    }
}
