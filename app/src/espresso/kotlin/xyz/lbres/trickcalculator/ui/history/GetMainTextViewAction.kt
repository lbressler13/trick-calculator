package xyz.lbres.trickcalculator.ui.history

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import xyz.lbres.trickcalculator.R

private var savedText: String? = null

private class GetMainTextViewAction : ViewAction {
    override fun getConstraints(): Matcher<View> = withId(R.id.mainText)

    override fun getDescription(): String = "getting text from main text"

    override fun perform(uiController: UiController?, view: View?) {
        if (view != null) {
            savedText = (view as TextView).text?.toString() ?: ""
        }
    }
}

fun getMainText(): String {
    onView(withId(R.id.mainText)).perform(GetMainTextViewAction())
    val text = savedText!!
    savedText = null

    return text
}
