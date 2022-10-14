package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.helpers.actionOnItemViewAtPosition
import xyz.lbres.trickcalculator.ui.attributions.authorattribution.AuthorAttributionViewHolder


/**
 * Click the expand/collapse button for an element in the main RecyclerView
 *
 * @param position [Int]: position of item to click
 */
fun expandCollapseAttribution(position: Int) {
    val recyclerId = R.id.attributionsRecycler

    onView(withId(recyclerId))
        .perform(RecyclerViewActions.scrollToPosition<AuthorAttributionViewHolder>(0))
    onView(withId(recyclerId)).perform(
        actionOnItemViewAtPosition(position, R.id.expandCollapseButton, ViewActions.click())
    )
}
