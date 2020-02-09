package ru.javawebinar.topjava.repository.mock

import ru.javawebinar.topjava.model.AbstractBaseEntity
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

open class InMemoryBaseRepository<T : AbstractBaseEntity> {
    val map: MutableMap<Int, T> = ConcurrentHashMap()
    private val counter: AtomicInteger = AtomicInteger(AbstractBaseEntity.START_SEQ)

    fun save(entry: T): T? = entry.run {
        if (isNew) {
            id = counter.getAndIncrement()
            map[id!!] = this
            return@run this
        }
        map.computeIfPresent(id!!) { _, _ ->
            this
        }
    }

    fun delete(id: Int): Boolean = map.remove(id) != null

    fun get(id: Int): T? = map[id]

    fun getCollection() = map.values

}