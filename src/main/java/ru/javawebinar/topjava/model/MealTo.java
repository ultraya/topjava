package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class MealTo {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final Supplier<Boolean> exceed;

    public MealTo(LocalDateTime dateTime, String description, int calories, Supplier<Boolean> exceed) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.exceed = exceed;
    }

    public Boolean getExceed() {
        return exceed.get();
    }

    @Override
    public String toString() {
        return "UserMealWithExceed{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", exceed=" + getExceed() +
                '}';
    }
}
