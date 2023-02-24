package xyz.lbres.trickcalculator.testutils.viewassertions

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion

private class MatchesAllViewAssertions(private val assertions: Array<out ViewAssertion>) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        assertions.forEach { it.check(view, noViewFoundException) }
    }
}

fun matchesAll(vararg assertions: ViewAssertion): ViewAssertion = MatchesAllViewAssertions(assertions)
