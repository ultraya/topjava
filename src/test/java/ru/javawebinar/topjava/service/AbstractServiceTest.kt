package ru.javawebinar.topjava.service

import org.junit.Assert
import org.junit.Assert.*
import org.junit.ClassRule
import org.junit.Rule
import org.junit.function.ThrowingRunnable
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.core.env.Environment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.junit4.SpringRunner
import ru.javawebinar.topjava.Profiles
import ru.javawebinar.topjava.TimingRules
import ru.javawebinar.topjava.config.CoreConfig
import ru.javawebinar.topjava.config.DaoConfig
import ru.javawebinar.topjava.util.ValidationUtil
import ru.javawebinar.topjava.util.ValidationUtil.getRootCause
import ru.javawebinar.topjava.util.exception.NotFoundException

//https://spring.io/blog/2011/06/21/spring-3-1-m2-testing-with-configuration-classes-and-profiles
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [CoreConfig::class, DaoConfig::class])
@Sql(value = ["classpath:db/populateDB.sql"], config = SqlConfig(encoding = "UTF-8"))
//@ActiveProfiles(resolver = ActiveDbProfileResolver::class)   //@ActiveProfiles(Profiles.HSQLDB)
@ActiveProfiles(value = [Profiles.POSTGRES])
abstract class AbstractServiceTest {

    @Autowired
    protected lateinit var cacheManager: CacheManager

    @Autowired
    protected lateinit var env: Environment

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)

        //https://kotlinlang.org/docs/reference/java-to-kotlin-interop.html#instance-fields
        @ClassRule
        @JvmField
        val summary = TimingRules.SUMMARY
    }

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    @Rule
    @JvmField
    val stopwatch = TimingRules.STOPWATCH

    protected fun expectedNotFoundException(msg: String) {
        expectedException.expect(NotFoundException::class.java)
        expectedException.expectMessage(msg)
    }

    //  Check root cause in JUnit: https://github.com/junit-team/junit4/pull/778
    protected fun <T : Throwable> validateRootCase(clazz: Class<T>, block: () -> Unit) {
        assertThrows(clazz) {
            try {
                block()
            } catch (e: Exception) {
                throw getRootCause(e)
            }

        }
    }
}