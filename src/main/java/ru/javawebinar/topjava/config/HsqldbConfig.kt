package ru.javawebinar.topjava.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.core.io.Resource
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import javax.sql.DataSource

@Configuration
@Profile("hsqldb")
@PropertySource("classpath:db/hsqldb.properties")
class HsqldbConfig : DSConfig {

    @Value("\${database.url}")
    lateinit var jdbcUrl: String
    @Value("\${database.username}")
    lateinit var username: String
    @Value("\${database.password}")
    lateinit var password: String
    @Value("classpath:db/initDB_hsql.sql")
    lateinit var initDb: Resource

    @Bean
    override fun dataSource(): DataSource =
            DriverManagerDataSource(jdbcUrl, username, password).apply {
                setDriverClassName("org.hsqldb.jdbcDriver")
            }

    @Bean
    fun dbInit() = DataSourceInitializer().apply {
        setDataSource(dataSource())
        setEnabled(true)
        setDatabasePopulator(
                ResourceDatabasePopulator().apply {
                    addScript(initDb)
                    setSqlScriptEncoding("utf-8")
                }
        )
    }
}