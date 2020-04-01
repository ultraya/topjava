package ru.javawebinar.topjava

import org.slf4j.LoggerFactory

class Profiles {
    companion object {
        const val POSTGRES = "postgres"
        const val HSQLDB = "hsqldb"
        const val JDBC = "jdbc"
        const val JPA = "jpa"
        const val DATAJPA = "datajpa"
        const val REPOSITORY_IMPLEMENTATION = "JPA"

        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)

        val activeProfile: String
            get() {
                return try {
                    Class.forName("org.postgresql.Driver")
                    POSTGRES
                } catch (ex: ClassNotFoundException) {
                    try {
                        Class.forName("org.hsqldb.jdbcDriver")
                        HSQLDB
                    } catch (ex: ClassNotFoundException) {
                        log.info("Driver DB not found in classpath")
                        throw ex
                    }
                }
            }
    }
}