package xyz.lbres.trickcalculator.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

// code adapted from this sample: https://github.com/dannyroa/espresso-samples/tree/master/RecyclerView/app/src/androidTest/java/com/dannyroa/espresso_samples/recyclerview
class ScrollToPositionViewAction(private val position: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> = Matchers.allOf(
        ViewMatchers.isAssignableFrom(RecyclerView::class.java),
        ViewMatchers.isDisplayed()
    )
    override fun getDescription(): String = "scroll RecyclerView to position: $position"

    override fun perform(uiController: UiController?, view: View) {
        view as RecyclerView
        view.scrollToPosition(position)
    }
}
