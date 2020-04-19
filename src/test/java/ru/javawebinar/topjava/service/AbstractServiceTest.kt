package ru.javawebinar.topjava.service

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.ExpectedException
import org.junit.rules.Stopwatch
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.junit4.SpringRunner
import ru.javawebinar.topjava.Profiles
import ru.javawebinar.topjava.config.CoreConfig
import ru.javawebinar.topjava.config.DaoConfig
import ru.javawebinar.topjava.util.exception.NotFoundException
import java.util.concurrent.TimeUnit

//https://spring.io/blog/2011/06/21/spring-3-1-m2-testing-with-configuration-classes-and-profiles
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [CoreConfig::class, DaoConfig::class])
@Sql(value = ["classpath:db/populateDB.sql"], config = SqlConfig(encoding = "UTF-8"))
//@ActiveProfiles(resolver = ActiveDbProfileResolver::class)   //@ActiveProfiles(Profiles.HSQLDB)
@ActiveProfiles(value = [Profiles.POSTGRES])
abstract class AbstractServiceTest {

    @Autowired
    protected lateinit var cacheManager: CacheManager

    companion object {

        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)

        val infoDuration = mutableMapOf<String, Long>()

        val result = StringBuilder()

        @BeforeClass
        @JvmStatic
        fun startTest() = println("TEST EXECUTING...")

        @AfterClass
        @JvmStatic
        fun endTest() {
            infoDuration.map {
                "${it.key}: ${TimeUnit.NANOSECONDS.toMillis(it.value)}"
            }.forEach {
                log.info(it)
            }
        }
    }

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    @Rule
    @JvmField
    val watcher = object : TestWatcher() {

        var start: Long = 0

        override fun starting(description: Description?) {
            start = System.nanoTime()
        }

        override fun finished(description: Description?) {
            infoDuration[description!!.methodName] = System.nanoTime() - start
        }
    }

    @Rule
    @JvmField
    val stopwatch = object : Stopwatch() {
        override fun finished(nanos: Long, description: Description?) {
            val s = String.format("\n%-25s %7d", description!!.methodName, TimeUnit.NANOSECONDS.toMillis(nanos))
            result.append(s)
            log.info(s)
        }
    }

    protected fun expectedNotFoundException(msg: String) {
        expectedException.expect(NotFoundException::class.java)
        expectedException.expectMessage(msg)
    }
}