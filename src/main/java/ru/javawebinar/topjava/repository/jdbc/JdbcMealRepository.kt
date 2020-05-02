package ru.javawebinar.topjava.repository.jdbc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.Profiles
import ru.javawebinar.topjava.model.Meal
import ru.javawebinar.topjava.repository.MealRepository
import ru.javawebinar.topjava.util.singleResult
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.annotation.PostConstruct

//https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#jdbc
abstract class JdbcMealRepository<T: Any> : MealRepository {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var namedJdbcTemplate: NamedParameterJdbcTemplate
    private lateinit var simpleJdbcInsert: SimpleJdbcInsert

    //поля для вставки определяются через отражение в бине и метаданные в SQL запросе
    private val ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal::class.java)

    @PostConstruct
    fun init() {
        simpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id")
    }

    /*
    can be fix update hsqldb version, this only for example
     */

    @Profile(Profiles.HSQLDB)
    @Repository
    class JdbcHsqldbMealRepository : JdbcMealRepository<Timestamp>() {
        override fun convertDate(dateTime: LocalDateTime): Timestamp {
            return Timestamp.valueOf(dateTime)
        }
    }

    @Profile(Profiles.POSTGRES)
    @Repository
    class JdbcPostgresMealRepository : JdbcMealRepository<LocalDateTime>() {
        override fun convertDate(dateTime: LocalDateTime): LocalDateTime {
            return dateTime
        }
    }

    abstract fun convertDate(dateTime: LocalDateTime): T

    override fun save(userId: Int, meal: Meal): Meal? {
        val params = mapOf(
                "id" to meal.id,
                "user_id" to userId,
                "date_time" to convertDate(meal.dateTime),
                "description" to meal.description,
                "calories" to meal.calories
        )
        if (meal.isNew) return meal.apply { id = simpleJdbcInsert.executeAndReturnKey(params).toInt() }
        return if (namedJdbcTemplate.update(
                        """UPDATE meals 
                      SET date_time = :date_time, 
                          description = :description,
                          calories = :calories
                    WHERE id = :id 
                      AND user_id = :user_id
                    """,
                        params
                ) == 0) null else meal

    }

    override fun delete(userId: Int, mealId: Int): Boolean =
            jdbcTemplate.update("DELETE FROM meals WHERE id = ? AND user_id = ?", mealId, userId) == 1

    override fun get(userId: Int, mealId: Int): Meal? =
            jdbcTemplate.query("SELECT * FROM meals WHERE id = ? AND user_id = ?", ROW_MAPPER, mealId, userId)
                    .singleResult

    override fun getAll(userId: Int): List<Meal> =
            jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC", ROW_MAPPER, userId)


    override fun getBetween(userId: Int, start: LocalDateTime, end: LocalDateTime): List<Meal> =
            jdbcTemplate.query(
                    "SELECT * FROM meals WHERE user_id = ? AND date_time BETWEEN ? AND ? ORDER BY date_time DESC",
                    ROW_MAPPER,
                    userId,
                    convertDate(start),
                    convertDate(end)
            )
}