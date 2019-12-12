package ru.javawebinar.topjava

import ru.javawebinar.topjava.model.Role
import ru.javawebinar.topjava.model.User

object UserTestData {

    const val USER_ID = 1
    const val ADMIN_ID = 2
    @JvmField
    val USER = User("User", "user@yandex.ru", "password", Role.ROLE_USER)
    @JvmField
    val ADMIN = User("Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN)
}