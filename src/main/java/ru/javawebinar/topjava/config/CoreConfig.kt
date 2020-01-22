package ru.javawebinar.topjava.config

import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["ru.javawebinar.**.service", "ru.javawebinar.**.web"])
class CoreConfig {

    @Bean
    fun methodInvokingFactoryBean() = MethodInvokingFactoryBean().apply {
        setStaticMethod("org.slf4j.bridge.SLF4JBridgeHandler.install")
    }
}