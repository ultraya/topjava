package ru.javawebinar.topjava

import org.junit.rules.ExternalResource
import org.junit.rules.Stopwatch
import org.junit.runner.Description
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

object TimingRules {

    private val log = LoggerFactory.getLogger("result")
    private val results = StringBuilder()
    private val DELIM = "-".repeat(103)

    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    val STOPWATCH: Stopwatch = object : Stopwatch() {
        override fun finished(nanos: Long, description: Description) {
            val result = String.format("%-95s %7d", description.displayName, TimeUnit.NANOSECONDS.toMillis(nanos))
            results.append(result).append('\n')
            log.info("$result ms\n")
        }
    }
    val SUMMARY: ExternalResource = object : ExternalResource() {
        override fun before() {
            results.setLength(0)
        }

        override fun after() {
            log.info("""

    $DELIM
    Test                                                                    Duration, ms
    $DELIM
    $results$DELIM

    """.trimIndent())
        }
    }
}