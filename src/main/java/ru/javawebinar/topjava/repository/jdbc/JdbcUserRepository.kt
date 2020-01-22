package ru.javawebinar.topjava.repository.jdbc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.model.User
import ru.javawebinar.topjava.repository.UserRepository
import ru.javawebinar.topjava.util.singleResult
import javax.annotation.PostConstruct

@Repository
class JdbcUserRepository : UserRepository {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private lateinit var simpleJdbcInsert: SimpleJdbcInsert

    private val ROW_MAPPER = BeanPropertyRowMapper.newInstance(User::class.java)

    @PostConstruct
    fun init() {
        simpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id")
    }

    override fun save(user: User): User? {
        val param = BeanPropertySqlParameterSource(user)  // all field have, but meal don't user_id (use custom map)

        if (user.isNew) {
            user.id = simpleJdbcInsert.executeAndReturnKey(param).toInt()
        } else {
            if (namedParameterJdbcTemplate.update(
                            """UPDATE users SET
                            name=:name,
                            email=:email,
                            password=:password,
                            registered=:registered,
                            enabled=:enabled,
                            calories_per_day=:caloriesPerDay
                            WHERE id=:id
                            """, param
                    ) == 0)
                return null
        }
        return user
    }

    override fun get(id: Int): User? =
            jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, id).singleResult

    override fun getByEmail(email: String): User? =
        jdbcTemplate.query("SELECT * FROM users WHERE email = ?", ROW_MAPPER, email).singleResult


    override fun delete(id: Int) = jdbcTemplate.update("DELETE FROM users WHERE id = ?", id) != 0

    override fun getAll(): List<User> {
//        fun allUsers(): List = jdbcTemplate.query("SELECT FIRST_NAME, LAST_NAME, EMAIL, PHONE FROM USERS",
//                { rs: ResultSet, _: Int ->
//                    User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("phone"))
//                })
        return jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER)
    }
}