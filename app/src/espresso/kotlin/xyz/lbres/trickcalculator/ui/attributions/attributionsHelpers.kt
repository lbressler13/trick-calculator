package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.viewactions.actionOnItemViewAtPosition
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

/**
 * Click the expand/collapse button for an element in the main RecyclerView
 *
 * @param position [Int]: position of item to click
 */
fun expandCollapseAttribution(position: Int) {
    onView(withId(R.id.attributionsRecycler)).perform(
        scrollToPosition(0),
        actionOnItemViewAtPosition(R.id.expandCollapseButton, position, click())
    )
}
