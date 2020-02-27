package ru.javawebinar.topjava.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.core.io.Resource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import javax.sql.DataSource

@Configuration
@Profile("postgres")
@PropertySource("classpath:db/postgres.properties")
class PostgresConfig : DSConfig {

    @Value("\${database.url}")
    lateinit var jdbcUrl: String
    @Value("\${database.username}")
    lateinit var username: String
    @Value("\${database.password}")
    lateinit var password: String
    @Value("classpath:db/initDB_postgres.sql")
    lateinit var initDb: Resource

    @Bean
    override fun dataSource(): DataSource = HikariDataSource(HikariConfig().also {
        it.driverClassName = "org.postgresql.Driver"
        it.jdbcUrl = this.jdbcUrl
        it.username = this.username
        it.password = this.password
    })

    @Bean
    fun dbInit() = DataSourceInitializer().apply {
        setDataSource(dataSource())
        setEnabled(true)
        setDatabasePopulator(
                ResourceDatabasePopulator().apply {
                    addScript(initDb)
//                    addScripts(
//                            ClassPathResource("/db/initDB_postgres.sql")
//                            ClassPathResource("/db/populateDB.sql")
//                    )
                    setSqlScriptEncoding("utf-8")
                }
        )
    }

    @Bean
    fun methodInvokingFactoryBean() = MethodInvokingFactoryBean().apply {
        setStaticMethod("org.slf4j.bridge.SLF4JBridgeHandler.install")
    }
}