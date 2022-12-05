package xyz.lbres.trickcalculator.testutils.matchers

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not

/**
 * Wrapper function for creating a [Matcher] for text with an empty string
 */
fun isEmptyString(): Matcher<View> = withText("")

/**
 * Wrapper function for creating a [Matcher] for text with a non-empty string
 */
fun isNotEmptyString(): Matcher<View> = not(withText(""))
