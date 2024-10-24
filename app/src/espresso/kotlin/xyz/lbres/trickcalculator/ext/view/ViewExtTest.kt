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
        var view = View(getContextEspresso(activityRule))
        view.visibility = View.INVISIBLE
        view.visible()
        assertEquals(View.VISIBLE, view.visibility)

        view = View(getContextEspresso(activityRule))
        view.visibility = View.GONE
        view.visible()
        assertEquals(View.VISIBLE, view.visibility)

        view = View(getContextEspresso(activityRule))
        view.visibility = View.VISIBLE
        view.visible()
        assertEquals(View.VISIBLE, view.visibility)
    }

    @Test
    fun invisible() {
        var view = View(getContextEspresso(activityRule))
        view.visibility = View.VISIBLE
        view.invisible()
        assertEquals(View.INVISIBLE, view.visibility)

        view = View(getContextEspresso(activityRule))
        view.visibility = View.GONE
        view.invisible()
        assertEquals(View.INVISIBLE, view.visibility)

        view = View(getContextEspresso(activityRule))
        view.visibility = View.INVISIBLE
        view.invisible()
        assertEquals(View.INVISIBLE, view.visibility)
    }

    @Test
    fun gone() {
        var view = View(getContextEspresso(activityRule))
        view.visibility = View.VISIBLE
        view.gone()
        assertEquals(View.GONE, view.visibility)

        view = View(getContextEspresso(activityRule))
        view.visibility = View.INVISIBLE
        view.gone()
        assertEquals(View.GONE, view.visibility)

        view = View(getContextEspresso(activityRule))
        view.visibility = View.GONE
        view.gone()
        assertEquals(View.GONE, view.visibility)
    }

    @Test
    fun enable() {
        var view = View(getContextEspresso(activityRule))
        view.isEnabled = false
        view.enable()
        assertTrue(view.isEnabled)

        view = View(getContextEspresso(activityRule))
        view.isEnabled = true
        view.enable()
        assertTrue(view.isEnabled)
    }

    @Test
    fun disable() {
        var view = View(getContextEspresso(activityRule))
        view.isEnabled = true
        view.disable()
        assertFalse(view.isEnabled)

        view = View(getContextEspresso(activityRule))
        view.isEnabled = false
        view.disable()
        assertFalse(view.isEnabled)
    }
}
