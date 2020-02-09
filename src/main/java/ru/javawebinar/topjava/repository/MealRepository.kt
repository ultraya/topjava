package ru.javawebinar.topjava.repository

import ru.javawebinar.topjava.model.Meal
import java.time.LocalDate
import java.time.LocalDateTime

interface MealRepository {
    // null if not found, when updated
    fun save(userId: Int, meal: Meal): Meal?

    // false if not found
    fun delete(userId: Int, mealId: Int): Boolean

    // null if not found
    fun get(userId: Int, mealId: Int): Meal?

    fun getAll(userId: Int): List<Meal>

    fun getBetween(userId: Int, start: LocalDateTime, end: LocalDateTime): List<Meal>
}