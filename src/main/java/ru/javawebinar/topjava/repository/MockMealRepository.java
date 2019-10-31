package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MockMealRepository implements MealRepository {

    private static final Map<Integer, Meal> repo = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(6);

    static {
        MealsUtil.MEALS.forEach(meal -> repo.merge(meal.getId(), meal, (l, r) -> l));
    }

    @Override
    public Meal get(int id) {
        return repo.get(id);
    }

    @Override
    public Meal save(Meal meal) {
        if(meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        repo.put(meal.getId(), meal);
        return repo.get(meal.getId());
    }

    @Override
    public void delete(int id) {
        repo.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(repo.values());
    }
}
