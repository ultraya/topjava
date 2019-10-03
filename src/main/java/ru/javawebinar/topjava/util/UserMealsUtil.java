package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        System.out.println(getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(getFilteredWithExceededCollectorInterface(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(getFilteredWithExceededCollectorOf(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

    }

    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> collect = mealList.stream()
                .collect(
                        Collectors.groupingBy(m -> m.getDateTime().toLocalDate(),
                                Collectors.summingInt(UserMeal::getCalories))
                );

        return mealList.stream()
                .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m ->
                        new UserMealWithExceed(
                                m.getDateTime(),
                                m.getDescription(),
                                m.getCalories(),
                                collect.get(m.getDateTime().toLocalDate()) > caloriesPerDay
                        )).collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceededCollectorInterface(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final class MealCollector implements Collector<UserMeal, Map<LocalDateTime, List<UserMeal>>, List<UserMealWithExceed>> {

            private Map<LocalDate, Integer> container = new ConcurrentHashMap<>();

            @Override
            public Supplier<Map<LocalDateTime, List<UserMeal>>> supplier() {
                return HashMap::new;
            }

            @Override
            public BiConsumer<Map<LocalDateTime, List<UserMeal>>, UserMeal> accumulator() {
                return (map, meal) -> {
                    List<UserMeal> meals = map.computeIfAbsent(meal.getDateTime(), key -> new ArrayList<>());
                    Integer result = container.computeIfAbsent(meal.getDateTime().toLocalDate(), key -> 0);
                    result += meal.getCalories();
                    container.put(meal.getDateTime().toLocalDate(), result);

                    if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                        meals.add(meal);
                    }
                };
            }

            @Override
            public BinaryOperator<Map<LocalDateTime, List<UserMeal>>> combiner() {
                return (left, right) -> {
                    left.forEach((key, value) -> {
                        right.merge(key, value, (oldValue, newValue) -> {
                            oldValue.addAll(newValue);
                            return oldValue;
                        });
                    });
                    return right;
                };
            }

            @Override
            public Function<Map<LocalDateTime, List<UserMeal>>, List<UserMealWithExceed>> finisher() {
                return map -> map.entrySet().stream()
                        .flatMap(
                                entry -> entry.getValue().stream()
                                        .map(meal -> new UserMealWithExceed(
                                                meal.getDateTime(),
                                                meal.getDescription(),
                                                meal.getCalories(),
                                                container.get(entry.getKey().toLocalDate()) > caloriesPerDay)))
                        .collect(Collectors.toList());
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED));
            }
        }
        return mealList.stream().collect(new MealCollector());
    }

    public static List<UserMealWithExceed> getFilteredWithExceededCollectorOf(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final class Aggregate {
            int sumOfExceed;
            private List<UserMeal> meals = new ArrayList<>();

            public void add(UserMeal meal) {
                sumOfExceed += meal.getCalories();
                if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                    meals.add(meal);
                }
            }

            public Aggregate combine(Aggregate that) {
                this.sumOfExceed += that.sumOfExceed;
                this.meals.addAll(that.meals);
                return this;
            }

            public Stream<UserMealWithExceed> finisher() {
                return meals.stream()
                        .map(m ->
                                new UserMealWithExceed(
                                        m.getDateTime(),
                                        m.getDescription(),
                                        m.getCalories(),
                                        sumOfExceed > caloriesPerDay
                                ));
            }
        }

        return mealList.stream()
                .collect(
                        Collectors.groupingBy(
                                m -> m.getDateTime().toLocalDate(),
                                Collector.of(Aggregate::new, Aggregate::add, Aggregate::combine, Aggregate::finisher)))
                .values().stream().
                        flatMap(Function.identity()).collect(Collectors.toList());
    }
}
