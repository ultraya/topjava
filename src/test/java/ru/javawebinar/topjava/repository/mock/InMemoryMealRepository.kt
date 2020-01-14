package ru.javawebinar.topjava.repository.mock

import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.ADMIN_ID
import ru.javawebinar.topjava.MEALS
import ru.javawebinar.topjava.USER_ID
import ru.javawebinar.topjava.model.Meal
import ru.javawebinar.topjava.repository.MealRepository
import ru.javawebinar.topjava.util.DateTimeUtil
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

@Repository("mockMealRepository")
class InMemoryMealRepository : MealRepository {
    private lateinit var repository: ConcurrentHashMap<Int, InMemoryBaseRepository<Meal>>

    fun init() {
        repository = ConcurrentHashMap()
        listOf(USER_ID, ADMIN_ID).forEach { user ->
            MEALS.forEach { this.save(user, it) }
        }
    }

    override fun save(userId: Int, meal: Meal): Meal? =
            repository.computeIfAbsent(userId) {
                InMemoryBaseRepository()
            }.save(meal)

    override fun delete(userId: Int, mealId: Int): Boolean {
        return repository[userId] != null && repository[userId]!!.delete(mealId)
    }

    override fun get(userId: Int, mealId: Int): Meal? {
        return repository[userId]?.get(mealId)
    }

    override fun getAll(userId: Int) = getWithFilter(userId)

    override fun getBetween(userId: Int, start: LocalDate, end: LocalDate) =
            getWithFilter(userId) {
                DateTimeUtil.isBetween(it.date, start, end)
            }

    private fun getWithFilter(id: Int, filter: (Meal) -> Boolean = { true }): Collection<Meal> =
            repository[id]?.let {
                it.getCollection().filter(filter).sortedWith(compareBy { it.dateTime })
            } ?: listOf()
}