package ru.javawebinar.topjava.repository.jdbc

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.Profiles
import java.sql.Timestamp
import java.time.LocalDateTime

@Profile(Profiles.HSQLDB)
@Repository
class JdbcHsqldbMealRepository : AbstractJdbcMealRepository<Timestamp>() {
    override fun convertDate(dateTime: LocalDateTime): Timestamp {
        return Timestamp.valueOf(dateTime)
    }
}