package ru.javawebinar.topjava.repository.inmemory

import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.model.Meal
import ru.javawebinar.topjava.repository.MealRepository
import ru.javawebinar.topjava.util.DateTimeUtil
import ru.javawebinar.topjava.util.MealsUtil
import ru.javawebinar.topjava.util.UsersUtil.Companion.USER_ID_1
import ru.javawebinar.topjava.util.UsersUtil.Companion.USER_ID_2
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Repository
class InMemoryMealRepository : MealRepository {
    private val repository = ConcurrentHashMap<Int, InMemoryBaseRepository<Meal>>()

    init {
        listOf(USER_ID_1, USER_ID_2).forEach { user ->
            MealsUtil.MEALS.forEach { this.save(user, it) }
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

    override fun getAll(userId: Int) =
            getWithFilter(userId) {
                true
            }

    override fun getBetween(userId: Int, start: LocalDate, end: LocalDate) =
            getWithFilter(userId) {
                DateTimeUtil.isBetween(it.date, start, end)
            }

    private fun getWithFilter(id: Int, filter: (Meal) -> Boolean): Collection<Meal> =
            repository[id]?.getFiltered(
                    filter,
                    compareBy { it.dateTime }
            ) ?: listOf()
}