package ru.javawebinar.topjava.repository.jdbc

import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.model.Meal
import ru.javawebinar.topjava.repository.MealRepository
import java.time.LocalDate

@Repository
class JdbcMealRepository: MealRepository {
    override fun save(userId: Int, meal: Meal): Meal? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(userId: Int, mealId: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(userId: Int, mealId: Int): Meal? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(userId: Int): Collection<Meal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBetween(userId: Int, start: LocalDate, end: LocalDate): Collection<Meal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}