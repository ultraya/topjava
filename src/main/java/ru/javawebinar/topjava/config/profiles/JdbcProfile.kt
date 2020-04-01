package ru.javawebinar.topjava.config.profiles

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.javawebinar.topjava.Profiles
import ru.javawebinar.topjava.config.DaoConfig

@Configuration
@Profile(Profiles.JDBC)
@ComponentScan(basePackages = ["ru.javawebinar.**.repository.jdbc"])
class JdbcProfile : DaoConfig() {

    @Bean
    fun jdbcTemplate() = JdbcTemplate(dsConfig.dataSource())

    @Bean
    fun namedParameterJdbcTemplate() = NamedParameterJdbcTemplate(dsConfig.dataSource())
}