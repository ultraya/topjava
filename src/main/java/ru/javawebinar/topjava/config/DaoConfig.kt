package ru.javawebinar.topjava.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.*
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
@ComponentScan(basePackages = ["ru.javawebinar.**.repository.jpa"])
@EnableTransactionManagement
@Import(value = [PostgresConfig::class, HsqldbConfig::class])
class DaoConfig {

    @Autowired
    private lateinit var dsConfig: DSConfig

    @Bean
    fun jdbcTemplate() = JdbcTemplate(dsConfig.dataSource())

    @Bean
    fun namedParameterJdbcTemplate() = NamedParameterJdbcTemplate(dsConfig.dataSource())

    @Bean
    fun entityManagerFactory(
            @Value("\${jpa.showSql}") showSql: Boolean,
            @Value("\${hibernate.format_sql}") formatSql: Boolean,
            @Value("\${hibernate.use_sql_comments}") sqlComments: Boolean
    ) = LocalContainerEntityManagerFactoryBean().apply {
        setDataSource(dsConfig.dataSource())
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
