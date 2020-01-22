package ru.javawebinar.topjava.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Meal : AbstractBaseEntity {
    var dateTime: LocalDateTime = LocalDateTime.MIN
    var description: String = ""
    var calories = 0

    constructor()
    constructor(m: Meal) : this(m.id, m.dateTime, m.description, m.calories)
    constructor(dateTime: LocalDateTime, description: String, calories: Int) : this(null, dateTime, description, calories)
    constructor(id: Int?, dateTime: LocalDateTime, description: String, calories: Int) : super(id) {
        this.dateTime = dateTime
        this.description = description
        this.calories = calories
    }

    val date: LocalDate
        get() = dateTime!!.toLocalDate()

    val time: LocalTime
        get() = dateTime!!.toLocalTime()

    override fun isNew(): Boolean {
        return id == null
    }

    override fun toString(): String {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}'
    }
}