package ru.javawebinar.topjava.repository.datajpa

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import ru.javawebinar.topjava.model.Meal
import java.time.LocalDateTime
import java.util.*

@Transactional(readOnly = true)
interface CrudMealRepository : JpaRepository<Meal, Int> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.id = :meal_id AND m.user.id = :user_id")
    fun delete(@Param("user_id") userId: Int, @Param("meal_id") mealId: Int): Int

    fun findByIdAndUserId(mealId: Int, userId: Int): Optional<Meal>

    fun findAllByUserId(userId: Int, sort: Sort): List<Meal>

    fun findByUserIdAndDateTimeBetween(userId: Int, start: LocalDateTime, end: LocalDateTime, sort: Sort): List<Meal>

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.user.id = :user_id AND m.id = :meal_id")
    fun getWithUser(@Param("user_id") userId: Int, @Param("meal_id") mealId: Int) : Optional<Meal>
}