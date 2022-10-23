package xyz.lbres.trickcalculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.testutils.isNotPresented

@RunWith(AndroidJUnit4::class)
class ProductFlavorTest {

    @Rule
    @JvmField
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun noDevToolsButton() {
        onView(withId(R.id.devToolsButton)).check(isNotPresented())
    }
}
