package ru.javawebinar.topjava.service

import org.springframework.stereotype.Service
import ru.javawebinar.topjava.model.Meal
import ru.javawebinar.topjava.repository.MealRepository
import ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId
import ru.javawebinar.topjava.util.exception.NotFoundException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class MealService(
        private val repository: MealRepository
) {
    @Throws(NotFoundException::class)
    fun get(userId: Int, mealId: Int) = checkNotFoundWithId(repository.get(userId, mealId), mealId)

    fun create(userId: Int, meal: Meal) = repository.save(userId, meal)

    @Throws(NotFoundException::class)
    fun update(userId: Int, meal: Meal) = checkNotFoundWithId(repository.save(userId, meal), meal.id)

    @Throws(NotFoundException::class)
    fun delete(userId: Int, mealId: Int) = checkNotFoundWithId(repository.delete(userId, mealId), mealId)

    fun getAll(userId: Int) = repository.getAll(userId)

    fun getFilteredByDate(userId: Int, start: LocalDate, end: LocalDate) =
            repository.getBetween(userId, LocalDateTime.of(start, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX))

    @Throws(NotFoundException::class)
    fun getWithUser(userId: Int, mealId: Int) = checkNotFoundWithId(repository.getWithUser(userId, mealId), mealId)
}