package xyz.lbres.trickcalculator.helpers.viewactions

import android.view.View
import androidx.core.view.isVisible
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

class ClickViewViewAction : ViewAction {
    override fun getConstraints(): Matcher<View> = ViewMatchers.isClickable()

    override fun getDescription(): String = "Clicking a View"

    override fun perform(uiController: UiController, view: View) {
        if (view.isVisible) {
            view.callOnClick()
        }
    }
}
