package ru.javawebinar.topjava;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMatcher<T> {

    private String[] fields;

    private TestMatcher(String... fields) {
        this.fields = fields;
    }

    public static <T> TestMatcher<T> usingFieldsComparator(String... fields) {
        return new TestMatcher<>(fields);
    }
    public void assertMatch(T actual, T expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, fields);
    }

    public void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, List.of(expected));
    }

    public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(fields).isEqualTo(expected);
    }
}
