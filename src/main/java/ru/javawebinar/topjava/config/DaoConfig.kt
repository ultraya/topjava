package ru.javawebinar.topjava.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import javax.sql.DataSource

//allopen fix
@Configuration
@PropertySource("classpath:db/postgres.properties")
@ComponentScan(basePackages = ["ru.javawebinar.**.repository.jdbc"])
class DaoConfig {

    @Bean
    fun dataSource(
            @Value("\${database.driver}") driverClassName: String,
            @Value("\${database.url}") jdbcUrl: String,
            @Value("\${database.username}") username: String,
            @Value("\${database.password}") password: String
    ): HikariDataSource = HikariDataSource(HikariConfig().apply {
        this.driverClassName = driverClassName
        this.jdbcUrl = jdbcUrl
        this.username = username
        this.password = password
    })

    @Bean
    fun jdbcTemplate(dataSource: HikariDataSource) = JdbcTemplate(dataSource)

    @Bean
    fun namedParameterJdbcTemplate(dataSource: HikariDataSource) = NamedParameterJdbcTemplate(dataSource)

    @Bean
    fun dbInit(dataSource: HikariDataSource) = DataSourceInitializer().apply {
        setDataSource(dataSource)
        setEnabled(true)
        setDatabasePopulator(
                ResourceDatabasePopulator().apply {
                    addScripts(
                            ClassPathResource("/db/initDB.sql")
                          //  ClassPathResource("/db/populateDB.sql")
                    )
                    setSqlScriptEncoding("utf-8")
                }
        )
    }
}
