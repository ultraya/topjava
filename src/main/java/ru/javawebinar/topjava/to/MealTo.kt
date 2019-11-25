package ru.javawebinar.topjava.to

import ru.javawebinar.topjava.model.Meal
import java.time.LocalDateTime

data class MealTo(
        val id: Int,
        val dateTime: LocalDateTime,
        val description: String,
        val calories: Int,
        val excess: Boolean? = null
) {
    companion object {
        fun from(meal: Meal) = MealTo(
                id = meal.id,
                dateTime = meal.dateTime,
                description = meal.description,
                calories = meal.calories
        )
    }

}