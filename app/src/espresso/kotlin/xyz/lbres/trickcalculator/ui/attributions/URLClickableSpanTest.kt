package xyz.lbres.trickcalculator.ui.attributions

import android.text.SpannableString
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.testutils.rules.RetryRule

@RunWith(AndroidJUnit4::class)
class URLClickableSpanTest {
    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(BaseActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule(maxRetries = 0)

    @Test
    fun addToText() {
        var string = SpannableString("Hello world")
        URLClickableSpan.addToText(string, "http://lbres.xyz")
        var spans = string.getSpans(0, string.length, URLClickableSpan::class.java)
        assertEquals(1, spans.size)

        string = SpannableString("")
        URLClickableSpan.addToText(string, "http://lbres.xyz")
        spans = string.getSpans(0, string.length, URLClickableSpan::class.java)
        assertEquals(1, spans.size)

        string = SpannableString("a")
        URLClickableSpan.addToText(string, "http://lbres.xyz")
        spans = string.getSpans(0, string.length, URLClickableSpan::class.java)
        assertEquals(1, spans.size)

        string = SpannableString("http://lbres.xyz")
        URLClickableSpan.addToText(string, "http://lbres.xyz")
        spans = string.getSpans(0, string.length, URLClickableSpan::class.java)
        assertEquals(1, spans.size)
    }

    @Test
    fun addToFirstOccurrence() {
        var string = SpannableString("Hello world")
        URLClickableSpan.addToFirstOccurrence(string, "Hello", "http://lbres.xyz")
        var spans = string.getSpans(0, string.length, URLClickableSpan::class.java)
        assertEquals(1, spans.size)

        string = SpannableString("Hello world")
        URLClickableSpan.addToFirstOccurrence(string, "Hello", "http://lbres.xyz")
        URLClickableSpan.addToFirstOccurrence(string, "world", "http://lbres.xyz")
        spans = string.getSpans(0, string.length, URLClickableSpan::class.java)
        assertEquals(2, spans.size)

        string = SpannableString("12345 6789 12345")
        URLClickableSpan.addToFirstOccurrence(string, "12345", "http://lbres.xyz")
        spans = string.getSpans(0, string.length, URLClickableSpan::class.java)
        assertEquals(1, spans.size)

        URLClickableSpan.addToFirstOccurrence(string, "12345", "http://lbres.xyz")
        spans = string.getSpans(0, string.length, URLClickableSpan::class.java)
        assertEquals(2, spans.size)

        string = SpannableString("Visit this site: http://lbres.xyz!")
        URLClickableSpan.addToFirstOccurrence(string, "http://lbres.xyz", "http://lbres.xyz")
        spans = string.getSpans(0, string.length, URLClickableSpan::class.java)
        assertEquals(1, spans.size)

        string = SpannableString("goodbye planet")
        URLClickableSpan.addToFirstOccurrence(string, "hello", "http://lbres.xyz")
        spans = string.getSpans(0, string.length, URLClickableSpan::class.java)
        assertEquals(0, spans.size)
    }
}
