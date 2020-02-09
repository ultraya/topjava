package ru.javawebinar.topjava

import org.assertj.core.api.Assertions.assertThat
import ru.javawebinar.topjava.model.AbstractBaseEntity.Companion.START_SEQ
import ru.javawebinar.topjava.model.Role
import ru.javawebinar.topjava.model.User


const val USER_ID = START_SEQ
const val ADMIN_ID = START_SEQ + 1
@JvmField
val USER = User(USER_ID, "User", "user@yandex.ru", "password", Role.ROLE_USER)
@JvmField
val ADMIN = User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN)

fun assertMatch(iterable: Iterable<User>, user: User, vararg users: User) {
    assertThat(iterable).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(listOf(user, *users))
}

fun assertMatch(actual: User, expected: User) {
    assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles")
}

