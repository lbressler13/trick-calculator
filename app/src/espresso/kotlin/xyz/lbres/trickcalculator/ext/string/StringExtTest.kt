package xyz.lbres.trickcalculator.ext.string

import android.text.style.UnderlineSpan
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.testutils.rules.RetryRule

@RunWith(AndroidJUnit4::class)
class StringExtTest {
    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(BaseActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule(maxRetries = 0)

    @Test
    fun underlined() {
        // must be tested in espresso tests to avoid compile errors from not mocking SpannableString
        val string = "Hello world"
        val underlined = string.underlined()
        val spans = underlined.getSpans(0, string.length, UnderlineSpan::class.java)
        assertEquals(1, spans.size)

        val span = spans[0]
        assertEquals(0, underlined.getSpanStart(span))
        assertEquals(string.length, underlined.getSpanEnd(span))
    }
}
