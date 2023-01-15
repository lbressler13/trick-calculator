package xyz.lbres.trickcalculator.testutils.rules

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import xyz.lbres.trickcalculator.utils.AppLogger

/**
 * [TestRule] to re-run a failed test, in order to reduce failures in flaky tests.
 *
 * Adapted from similar rule defined by Andres Sandoval in this article:
 * https://andresand.medium.com/retry-testrule-for-android-espresso-tests-74683ee3b845
 *
 * @param maxRetries [Int]: number of times to re-try failing test, not including the initial test run.
 * Defaults to 3.
 */
// TODO restore default = 3
class RetryRule(val maxRetries: Int = 0) : TestRule {
    /**
     * Apply rule to a test with a given description
     *
     * @param test [Statement]: test to run
     * @param description [Description]: description of test
     * @return [Statement]
     */
    override fun apply(test: Statement, description: Description) = runTest(test, description)

    /**
     * Get a statement to run and retry a test
     *
     * @param test [Statement]: test to run
     * @param description [Description]: description of test
     * @return [Statement]
     */
    private fun runTest(test: Statement, description: Description): Statement {
        return object : Statement() {
            /**
             * Run the test, and retry on failure based on [maxRetries]
             * @throws [Throwable] if test fails on all retries
             */
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
