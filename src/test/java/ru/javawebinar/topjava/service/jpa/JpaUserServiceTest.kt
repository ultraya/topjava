package ru.javawebinar.topjava.service.jpa

import org.springframework.test.context.ActiveProfiles
import ru.javawebinar.topjava.Profiles
import ru.javawebinar.topjava.service.UserServiceTest

@ActiveProfiles(value = [Profiles.JPA])
class JpaUserServiceTest : UserServiceTest()