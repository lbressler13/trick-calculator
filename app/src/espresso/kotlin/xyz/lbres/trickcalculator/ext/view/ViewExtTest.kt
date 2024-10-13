package xyz.lbres.trickcalculator.ext.view

import android.view.View
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.testutils.getContextEspresso

@RunWith(AndroidJUnit4::class)
class ViewExtTest {
    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(BaseActivity::class.java)

    @Test
    fun visible() {
        val view = View(getContextEspresso(activityRule))
        view.visibility = View.INVISIBLE
        view.visible()
        assertEquals(View.VISIBLE, view.visibility)
    }

    @Test
    fun invisible() {
        val view = View(getContextEspresso(activityRule))
        view.visibility = View.VISIBLE
        view.invisible()
        assertEquals(View.INVISIBLE, view.visibility)
    }

    @Test
    fun gone() {
        val view = View(getContextEspresso(activityRule))
        view.visibility = View.VISIBLE
        view.gone()
        assertEquals(View.GONE, view.visibility)
    }

    @Test
    fun enable() {
        val view = View(getContextEspresso(activityRule))
        view.isEnabled = false
        view.enable()
        assertTrue(view.isEnabled)
    }

    @Test
    fun disable() {
        val view = View(getContextEspresso(activityRule))
        view.isEnabled = true
        view.disable()
        assertFalse(view.isEnabled)
    }
}
