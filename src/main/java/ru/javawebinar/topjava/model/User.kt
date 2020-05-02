package ru.javawebinar.topjava.model

import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.validator.constraints.Range
import ru.javawebinar.topjava.util.MealsUtil
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

//https://stackoverflow.com/questions/45642181/kotlin-jpa-encapsulate-onetomany
@Entity
@Table(name = "users")
@NamedQueries(
        NamedQuery(name = DELETE_USER, query = "DELETE FROM User u WHERE u.id =:id"),
        NamedQuery(name = BY_EMAIL_USER, query = "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email"),
        NamedQuery(name = ALL_SORTED_USERS, query = "SELECT u FROM User u LEFT JOIN FETCH u.roles ORDER BY u.name, u.email")
)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class User(
        id: Int? = null,
        name: String,

        @field:Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        @field:NotBlank
        @field:Size(max = 100)
        @Column(name = "email", nullable = false, unique = true)
        var email: String,

        @field:NotBlank
        @field:Size(min = 5)
        @Column(name = "password", nullable = false)
        var password: String,

        @field:NotNull
        @field:Range(min = 10, max = 10000)
        @Column(name = "calories_per_day", nullable = false)
        var caloriesPerDay: Int = MealsUtil.DEFAULT_CALORIES_PER_DAY,

        @field:NotNull
        @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
        var enabled: Boolean? = true,

        @field:NotNull
        @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
        var registered: Date? = Date(),

        @Enumerated(EnumType.STRING)
        @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
        @Column(name = "role")
        @ElementCollection(fetch = FetchType.EAGER)
        @BatchSize(size = 200)
        @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
        var roles: MutableSet<Role> = EnumSet.noneOf(Role::class.java)

) : AbstractNamedEntity(id, name) {


    constructor(u: User) :
            this(u.id, u.name, u.email, u.password, u.caloriesPerDay, u.enabled, u.registered, u.roles)

    constructor(id: Int?, name: String, email: String, password: String, role: Role, vararg roles: Role) :
            this(id, name, email, password, roles = EnumSet.of(role, *roles))


    @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
    @OrderBy("dateTime DESC")
    private var _meals = mutableListOf<Meal>()

    val meals
        get() = _meals.toList()

    fun addMeal(meal: Meal) = _meals.add(meal)

    override fun toString(): String {
        return "User(id='$id', name='$name', email='$email', password='$password', caloriesPerDay=$caloriesPerDay, enabled=$enabled, registered=$registered, roles=$roles)"
    }
}