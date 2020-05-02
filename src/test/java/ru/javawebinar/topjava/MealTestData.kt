package ru.javawebinar.topjava

import org.assertj.core.api.Assertions
import ru.javawebinar.topjava.model.AbstractBaseEntity.Companion.START_SEQ
import ru.javawebinar.topjava.model.Meal
import java.time.LocalDateTime
import java.time.Month

@JvmField
val MEALS = listOf(
        Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
        Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
        Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
        Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
        Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
        Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 503)
)

val MEAL_ID1 = START_SEQ + 2
val MEAL_ADMIN_ID = START_SEQ + 8

val MEAL1 = Meal(MEAL_ID1, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500)
val MEAL2 = Meal(MEAL_ID1 + 1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000)
val MEAL3 = Meal(MEAL_ID1 + 2, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500)
val MEAL4 = Meal(MEAL_ID1 + 3, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 500)
val MEAL5 = Meal(MEAL_ID1 + 4, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 1000)
val MEAL6 = Meal(MEAL_ID1 + 5, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 503)

val USER_MEALS
    get() = listOf(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1)

fun getCreated() = Meal(
        LocalDateTime.of(2020, Month.JANUARY, 21, 0, 0),
        "new description",
        1000
)

fun getUpdated() = Meal(MEAL_ID1, MEAL1.dateTime, "updated", 200)