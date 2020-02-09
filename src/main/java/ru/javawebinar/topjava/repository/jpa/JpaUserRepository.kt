package ru.javawebinar.topjava.repository.jpa

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.javawebinar.topjava.model.ALL_SORTED_USERS
import ru.javawebinar.topjava.model.BY_EMAIL_USER
import ru.javawebinar.topjava.model.DELETE_USER
import ru.javawebinar.topjava.model.User
import ru.javawebinar.topjava.repository.UserRepository
import ru.javawebinar.topjava.util.singleResult
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(readOnly = true)
class JpaUserRepository : UserRepository {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    override fun save(user: User): User? = with(em) {
        if (user.isNew) {
            persist(user)
            return user
        } else {
            merge(user)
        }
    }

    @Transactional
    override fun delete(id: Int): Boolean =
//            em.createQuery("DELETE FROM User u WHERE u.id =:id").setParameter("id", id).executeUpdate() != 0
            em.createNamedQuery(DELETE_USER)
                    .setParameter("id", id)
                    .executeUpdate() != 0

    override fun get(id: Int): User? = em.find(User::class.java, id)

    override fun getByEmail(email: String): User? =
//            em.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email").setParameter("email", email).single()
            em.createNamedQuery(BY_EMAIL_USER, User::class.java)
                    .setParameter("email", email)
                    .resultList
                    .singleResult

    override fun getAll(): List<User> =
            //em.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles ORDER BY u.name, u.email", User::class.java).resultList
            em.createNamedQuery(ALL_SORTED_USERS, User::class.java)
                    .resultList
}