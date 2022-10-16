package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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
    onView(withId(R.id.attributionsRecycler)).perform(
        RecyclerViewActions.scrollToPosition<AuthorAttributionViewHolder>(0),
        actionOnItemViewAtPosition(position, R.id.expandCollapseButton, click())
    )
}
