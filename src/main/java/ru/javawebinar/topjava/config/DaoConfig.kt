package ru.javawebinar.topjava.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.hibernate.cfg.AvailableSettings
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
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

//allopen fix (use spring plugin)
@Configuration
@PropertySource("classpath:db/postgres.properties")
@ComponentScan(basePackages = ["ru.javawebinar.**.repository.jpa"])
@EnableTransactionManagement
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

    @Bean
    fun entityManagerFactory(
            dataSource: HikariDataSource,
            @Value("\${jpa.showSql}") showSql: Boolean,
            @Value("\${hibernate.format_sql}") formatSql: Boolean,
            @Value("\${hibernate.use_sql_comments}") sqlComments: Boolean
    ) = LocalContainerEntityManagerFactoryBean().apply {
        setDataSource(dataSource)
        setPackagesToScan("ru.javawebinar.**.model")
        jpaVendorAdapter = HibernateJpaVendorAdapter().apply {
            setShowSql(showSql)
        }
        setJpaPropertyMap(mapOf(
                AvailableSettings.FORMAT_SQL to formatSql,
                AvailableSettings.JPA_PROXY_COMPLIANCE to false
                // , AvailableSettings.USE_SQL_COMMENTS to sqlComments
        )
        )
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory) = JpaTransactionManager().apply {
        setEntityManagerFactory(entityManagerFactory)
    }
}
