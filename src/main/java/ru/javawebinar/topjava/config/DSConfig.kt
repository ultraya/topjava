package ru.javawebinar.topjava.config

import javax.sql.DataSource

interface DSConfig {
    fun dataSource(): DataSource
}