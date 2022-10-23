package xyz.lbres.trickcalculator.testutils.rules

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import xyz.lbres.trickcalculator.utils.AppLogger

/**
 * [TestRule] to re-run a failed test, to reduce failures in flaky tests.
 *
 * Adapted from similar rule defined by Andres Sandoval in this article:
 * https://andresand.medium.com/retry-testrule-for-android-espresso-tests-74683ee3b845
 *
 * @param maxRetries [Int]: number of times to re-try failing test, not including the initial test run.
 * Defaults to 2.
 */
class RetryRule(val maxRetries: Int = 2) : TestRule {
    override fun apply(test: Statement, description: Description) = runTest(test, description)

    private fun runTest(test: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                var error: Throwable? = null

                for (i in 0..maxRetries) {
                    try {
                        test.evaluate()
                        return
                    } catch (t: Throwable) {
                        error = t

                        val remainingTries = maxRetries - i - 1
                        AppLogger.e(
                            "RetryRule",
                            "${description.displayName} failed, $remainingTries retries remaining"
                        )
                    }
                }

                throw error!!
            }
        }
    }
}
