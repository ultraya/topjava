package ru.javawebinar.topjava.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.javawebinar.topjava.config.profiles.DataJpaProfile
import ru.javawebinar.topjava.config.profiles.JdbcProfile
import ru.javawebinar.topjava.config.profiles.JpaProfile

//allopen fix (use spring plugin)
@Configuration
@Import(value = [
    PostgresConfig::class,
    HsqldbConfig::class,
    JpaConfig::class,
    JpaProfile::class,
    JdbcProfile::class,
    DataJpaProfile::class
])
abstract class DaoConfig {

    @Autowired
    lateinit var dsConfig: DSConfig
}
