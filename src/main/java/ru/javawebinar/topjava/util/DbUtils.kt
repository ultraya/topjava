package ru.javawebinar.topjava.util

import org.springframework.dao.support.DataAccessUtils
import javax.persistence.Query

val <T> List<T>.singleResult
        get() = DataAccessUtils.singleResult(this)

inline fun <reified T> Query.single(): T = DataAccessUtils.singleResult(resultList) as T