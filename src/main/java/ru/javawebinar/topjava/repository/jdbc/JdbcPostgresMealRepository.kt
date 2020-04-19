package ru.javawebinar.topjava.repository.jdbc

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.Profiles
import java.time.LocalDateTime

@Profile(Profiles.POSTGRES)
@Repository
class JdbcPostgresMealRepository : AbstractJdbcMealRepository<LocalDateTime>() {
    override fun convertDate(dateTime: LocalDateTime): LocalDateTime {
        return dateTime
    }
}