package ru.javawebinar.topjava.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource

@Configuration
@ImportResource("/spring/spring-db-test.xml")
class MockConfig {
}