package ru.javawebinar.topjava.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.jcache.JCacheCacheManager
import org.springframework.cache.jcache.JCacheManagerFactoryBean
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource


@Configuration
@ComponentScan(basePackages = ["ru.javawebinar.**.service", "ru.javawebinar.**.web"])
@EnableCaching
class CoreConfig {

    @Bean  // configure and return an implementation of Spring's CacheManager SPI
    fun cacheManager(): JCacheCacheManager =
            JCacheCacheManager().apply {
                cacheManager = JCacheManagerFactoryBean().apply {
                    setCacheManagerUri(ClassPathResource("cache/ehcache.xml").uri)
                }.`object`
            }
}

