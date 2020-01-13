package ru.javawebinar.topjava.util

import org.springframework.dao.support.DataAccessUtils

val <T> List<T>.singleResult
        get() = DataAccessUtils.singleResult(this)