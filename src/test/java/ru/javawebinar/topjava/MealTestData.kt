package ru.javawebinar.topjava

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
        Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
)