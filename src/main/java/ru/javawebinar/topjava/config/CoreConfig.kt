package ru.javawebinar.topjava.config

import org.springframework.context.annotation.ComponentScan

@ComponentScan(basePackages = ["ru.javawebinar.**.service", "ru.javawebinar.**.web"])
class CoreConfig {

//    @Bean
//    fun propertyConfigurer() = PropertySourcesPlaceholderConfigurer()
}