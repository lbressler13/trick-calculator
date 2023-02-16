package xyz.lbres.trickcalculator.testutils.viewactions

import android.view.View
import android.widget.TextView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher

private class ClickLinkInTextWithViewIdViewAction(private val textViewId: Int, private val textToClick: String) : ViewAction {
    override fun getConstraints(): Matcher<View> = isDisplayed()
    override fun getDescription(): String = "clicking a URLClickableSpan with text $textToClick"

    override fun perform(uiController: UiController?, view: View) {
        val textView = view.findViewById<TextView>(textViewId)
        clickLinkInText(textToClick).perform(uiController, textView)
    }
}

fun clickLinkInTextWithViewId(textViewId: Int, textToClick: String): ViewAction = ClickLinkInTextWithViewIdViewAction(textViewId, textToClick)
