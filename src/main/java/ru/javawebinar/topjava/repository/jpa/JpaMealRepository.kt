package ru.javawebinar.topjava.repository.jpa

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.javawebinar.topjava.model.*
import ru.javawebinar.topjava.repository.MealRepository
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(readOnly = true)
class JpaMealRepository : MealRepository {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    override fun save(userId: Int, meal: Meal): Meal? {
        if (!meal.isNew && get(userId, meal.id!!) == null)
            return null
        meal.user = em.getReference(User::class.java, userId)

        if (meal.isNew) {
            em.persist(meal)
            return meal
        }

        return em.merge(meal)
    }

    @Transactional
    override fun delete(userId: Int, mealId: Int): Boolean =
            //em.createQuery("DELETE FROM Meal m WHERE m.id =:id AND m.user.id =:user_id").setParameter("id", mealId).setParameter("user_id", userId).executeUpdate() != 0
            em.createNamedQuery(DELETE_MEAL)
                    .setParameter("id", mealId)
                    .setParameter("user_id", userId)
                    .executeUpdate() != 0

    override fun get(userId: Int, mealId: Int): Meal? =
            em.find(Meal::class.java, mealId)?.also {
                if (it.user?.id != userId)
                    return null
            }

    override fun getAll(userId: Int): List<Meal> =
            //em.createQuery("SELECT m FROM Meal m WHERE m.user.id =:user_id ORDER BY m.dateTime DESC", Meal::class.java).setParameter("user_id", userId).resultList
            em.createNamedQuery(ALL_SORTED_MEALS, Meal::class.java)
                    .setParameter("user_id", userId)
                    .resultList

    override fun getBetween(userId: Int, start: LocalDateTime, end: LocalDateTime): List<Meal> =
            //em.createQuery("""SELECT m FROM Meal m WHERE m.user.id =:user_id AND m.dateTime BETWEEN :start AND :end ORDER BY m.dateTime DESC""", Meal::class.java).setParameter("user_id", userId).setParameter("start", start).setParameter("end", end).resultList
            em.createNamedQuery(BETWEEN_MEALS, Meal::class.java)
                    .setParameter("user_id", userId)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .resultList
}