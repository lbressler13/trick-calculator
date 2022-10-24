package xyz.lbres.trickcalculator.utils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.MainActivity
import xyz.lbres.trickcalculator.testutils.openAttributionsFragment
import xyz.lbres.trickcalculator.testutils.rules.RetryRule

@RunWith(AndroidJUnit4::class)
class UiUtilsTest {
    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule()

    @Test
    fun createUnderlineText() {
        openAttributionsFragment()
        onView(withText("Expand")).check(matches(isUnderlined()))
    }
}
