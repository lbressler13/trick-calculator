package xyz.lbres.trickcalculator.ui.history

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import xyz.lbres.trickcalculator.R

/**
 * Most recently saved text. Should be reset after being retrieved once.
 */
private var savedText: String? = null

/**
 * [ViewAction] to extract the text from the main TextView and save it in a local variable.
 * This is designed specifically to retrieve the randomized result in the main view and save it in a history item.
 */
private class GetMainTextViewAction : ViewAction {
    override fun getConstraints(): Matcher<View> = withId(R.id.mainText)

    override fun getDescription(): String = "getting text from main text"

    override fun perform(uiController: UiController?, view: View?) {
        if (view != null) {
            savedText = (view as TextView).text?.toString() ?: ""
        }
    }
}

/**
 * Get the text from the main TextView
 *
 * @return [String]
 */
fun getMainText(): String {
    onView(withId(R.id.mainText)).perform(GetMainTextViewAction())
    val text = savedText!!
    // reset saved text
    savedText = null

    return text
}
