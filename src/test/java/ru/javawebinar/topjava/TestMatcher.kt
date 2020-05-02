package ru.javawebinar.topjava

import org.assertj.core.api.Assertions
import ru.javawebinar.topjava.model.Meal
import ru.javawebinar.topjava.model.User


class TestMatcher<T> private constructor(private vararg val fields: String) {

    companion object {
        fun <T> usingFieldsComparator(vararg fields: String) = TestMatcher<T>(*fields)
    }

    fun assertMatch(actual: T, expected: T) {
        Assertions.assertThat(actual).isEqualToIgnoringGivenFields(expected, *fields)
    }

    fun assertMatch(actual: Iterable<T>, obj1: T, vararg objs: T) {
        assertMatch(actual, listOf(obj1, *objs))
    }

    fun assertMatch(actual: Iterable<T>, expected: Iterable<T>) {
        Assertions.assertThat(actual).usingElementComparatorIgnoringFields(*fields).isEqualTo(expected)
    }

}

val USER_MATCHER = TestMatcher.usingFieldsComparator<User>("registered", "roles", "_meals")

val MEAL_MATCHER = TestMatcher.usingFieldsComparator<Meal>("user")