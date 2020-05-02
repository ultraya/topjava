package ru.javawebinar.topjava.model

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.validator.constraints.Range
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "meals")
@NamedQueries(
        NamedQuery(name = DELETE_MEAL, query = "DELETE FROM Meal m WHERE m.id =:id AND m.user.id =:user_id"),
        NamedQuery(name = ALL_SORTED_MEALS, query = "SELECT m FROM Meal m WHERE m.user.id =:user_id ORDER BY m.dateTime DESC"),
        NamedQuery(name = BETWEEN_MEALS, query = "SELECT m FROM Meal m WHERE m.user.id =:user_id AND m.dateTime BETWEEN :start AND :end ORDER BY m.dateTime DESC")
)
class Meal(
        id: Int? = null,
        @field:NotNull  //https://stackoverflow.com/questions/36515094/kotlin-and-valid-spring-annotation
        @field:Column(name = "date_time", nullable = false, columnDefinition = "timestamp default now()")
        var dateTime: LocalDateTime,

        @field:NotBlank
        @field:Size(max = 255)
        @Column(name = "description", nullable = false)
        var description: String,

        @field:NotNull
        @field:Range(min = 10, max = 5000)
        @Column(name = "calories", nullable = false)
        var calories: Int

) : AbstractBaseEntity(id) {

    constructor(m: Meal) :
            this(m.id, m.dateTime, m.description, m.calories)

    constructor(dateTime: LocalDateTime, description: String, calories: Int) :
            this(null, dateTime, description, calories)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  //(Optional) The name of the foreign key column.
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: User? = null

    val date: LocalDate
        get() = dateTime.toLocalDate()

    val time: LocalTime
        get() = dateTime.toLocalTime()

    override fun toString(): String {
        return "Meal(id='$id', dateTime=$dateTime, description='$description', calories=$calories)"
    }
}