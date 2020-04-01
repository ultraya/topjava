package ru.javawebinar.topjava.config.profiles

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import ru.javawebinar.topjava.Profiles
import ru.javawebinar.topjava.config.JpaConfig

@Configuration
@Profile(Profiles.DATAJPA)
@EnableJpaRepositories(basePackages = ["ru.javawebinar.**.repository.datajpa"])
@ComponentScan(basePackages = ["ru.javawebinar.**.repository.datajpa"])
class DataJpaProfile : JpaConfig()