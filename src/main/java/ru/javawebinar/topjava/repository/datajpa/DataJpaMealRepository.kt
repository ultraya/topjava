package ru.javawebinar.topjava.repository.datajpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.model.Meal
import ru.javawebinar.topjava.repository.MealRepository
import java.time.LocalDateTime

@Repository
class DataJpaMealRepository : MealRepository {

    @Autowired
    private lateinit var repoMeal: CrudMealRepository

    @Autowired
    private lateinit var repoUser: CrudUserRepository

    companion object {
        //private static final
        val SORT_DATETIME = Sort.by(Sort.Direction.DESC, "dateTime")
    }

    override fun save(userId: Int, meal: Meal): Meal? {
        if (!meal.isNew && get(userId, meal.id!!) == null) {
            return null
        }
        meal.user = repoUser.getOne(userId)
        return repoMeal.save(meal)
    }

    override fun delete(userId: Int, mealId: Int) =
            repoMeal.delete(userId, mealId) != 0

    override fun get(userId: Int, mealId: Int) =
            repoMeal.findByIdAndUserId(mealId, userId).orElse(null)

    override fun getAll(userId: Int) =
            repoMeal.findAllByUserId(userId, SORT_DATETIME)

    override fun getBetween(userId: Int, start: LocalDateTime, end: LocalDateTime) =
            repoMeal.findByUserIdAndDateTimeBetween(userId, start, end, SORT_DATETIME)

    override fun getWithUser(userId: Int, mealId: Int): Meal? {
        return repoMeal.getWithUser(userId, mealId).orElse(null)
    }
}