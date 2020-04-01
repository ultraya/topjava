package ru.javawebinar.topjava.config

import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory

@Configuration
@EnableTransactionManagement
abstract class JpaConfig : DaoConfig() {

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