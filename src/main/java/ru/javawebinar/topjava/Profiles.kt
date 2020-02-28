package ru.javawebinar.topjava

class Profiles{
    companion object {
        const val POSTGRES = "postgres"
        const val HSQLDB = "hsqldb"
        const val REPOSITORY_IMPLEMENTATION = "JPA"
    }
    val activeProfile: String
        get() {
            try {
                Class.forName("org.postgresql.Driver")
                return POSTGRES
            }catch (ex: ClassNotFoundException){
                Class.forName("org.hsqldb.jdbcDriver")
                return HSQLDB
            }
        }
}