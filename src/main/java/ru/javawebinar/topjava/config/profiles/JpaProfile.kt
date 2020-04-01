package ru.javawebinar.topjava.config.profiles

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import ru.javawebinar.topjava.Profiles
import ru.javawebinar.topjava.config.JpaConfig

@Configuration
@Profile(Profiles.JPA)
@ComponentScan(basePackages = ["ru.javawebinar.**.repository.jpa"])
class JpaProfile : JpaConfig()