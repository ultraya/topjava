package ru.javawebinar.topjava.util

import ru.javawebinar.topjava.model.AbstractBaseEntity
import ru.javawebinar.topjava.util.exception.NotFoundException

object ValidationUtil {
    @JvmStatic
    fun <T> checkNotFoundWithId(`object`: T?, id: Int?): T {
        return checkNotFound(`object`, "id=$id")
    }

    @JvmStatic
    fun checkNotFoundWithId(found: Boolean, id: Int) {
        checkNotFound(found, "id=$id")
    }

    @JvmStatic
    fun <T> checkNotFound(`object`: T?, msg: String): T {
        return `object` ?: throw NotFoundException("Not found entity with $msg")
    }

    @JvmStatic
    fun checkNotFound(found: Boolean, msg: String) {
        if (!found) {
            throw NotFoundException("Not found entity with $msg")
        }
    }

    @JvmStatic
    fun checkNew(entity: AbstractBaseEntity) {
        require(entity.isNew) { "$entity must be new (id=null)" }
    }

    @JvmStatic
    fun assureIdConsistent(entity: AbstractBaseEntity, id: Int) {
        //conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew) {
            entity.id = id
        } else require(entity.id == id) { "$entity must be with id=$id" }
    }

    //  http://stackoverflow.com/a/28565320/548473
    @JvmStatic
    fun getRootCause(t: Throwable): Throwable {
        var result = t
        var cause: Throwable?

        while (null != result.cause.also { cause = it } && result !== cause) {
            result = cause!!
        }
        return result
    }
}