package xyz.lbres.trickcalculator.testutils

import android.view.View
import androidx.test.espresso.ViewAction
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

/**
 * Most recently created [TextSaver], or null if none has been created
 */
private var lastSaver: TextSaver? = null

/**
 * Wrapper function for a [ViewAction] that saves the text of TextView.
 * Creates a new [TextSaver], and uses it to save text from TextView.
 * TextSaver is also stored in a local variable.
 *
 * @return [ViewAction]: the [TextSaver.SaveTextViewAction] associated with the new [TextSaver]
 */
fun saveText(): ViewAction {
    return TextSaver.saveText()
//    val saver = TextSaver()
//    lastSaver = saver
//    return saver.SaveTextViewAction()
}

/**
 * Wrapper function to get a matcher that checks if the text in a view matches the most recent saved text
 *
 * @return [Matcher]<[View]?>: the [TextSaver.PreviousTextViewMatcher] associated with the current [TextSaver],
 * or a matcher that always returns false if there is no saved matcher
 */
fun withSavedText(): Matcher<View?> {
    return TextSaver.withSavedText()
}

/**
 * Delete current stored [TextSaver]
 */
fun clearSavedText() {
    TextSaver.clearSavedText()
}
