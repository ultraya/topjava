package ru.javawebinar.topjava.util

import ru.javawebinar.topjava.model.Role
import ru.javawebinar.topjava.model.User

class UsersUtil {
    companion object {
        @JvmField
        val USER_ID_1 = 1
        @JvmField
        val USER_ID_2 = 2
        @JvmField
        val USER_1 = User("USER_1", "user1@email.com", "pass_user1", Role.ROLE_USER)
        @JvmField
        val USER_2 = User("USER_2", "user2@email.com", "pass_user2", Role.ROLE_USER)
        @JvmField
        val ADMIN = User("ADMIN", "admin@email.com", "pass_admin", Role.ROLE_ADMIN)
    }
}