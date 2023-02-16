package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.matchers.withNestedViewHolder
import xyz.lbres.trickcalculator.testutils.viewactions.actionOnItemViewAtPosition
import xyz.lbres.trickcalculator.testutils.viewactions.actionOnNestedItemViewAtPosition
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions

private val imageUrls = authorAttributions.map { it.images.map { it.url } }
private const val recyclerId = R.id.attributionsRecycler
private const val nestedRecyclerId = R.id.imagesRecycler

/**
 * Click the expand/collapse button for an element in the main RecyclerView
 *
 * @param position [Int]: position of item to click
 */
fun expandCollapseAttribution(position: Int) {
    onView(withId(recyclerId)).perform(
        scrollToPosition(0),
        actionOnItemViewAtPosition(R.id.expandCollapseButton, position, click())
    )
}

/**
 * Check that image urls for specified dropdowns are not visible
 *
 * @param positions [IntList]: list of positions where image urls should not be visible
 */
fun checkImagesNotPresented(positions: IntList) {
    for (position in positions) {
        onView(withId(recyclerId)).perform(scrollToPosition(0))
        for (url in imageUrls[position]) {
            onView(withText(url)).check(isNotPresented())
        }
    }
}

/**
 * Check that image urls for specified dropdowns are visible
 *
 * @param positions [IntList]: list of positions where image urls should be visible
 */
fun checkImagesDisplayed(positions: IntList) {
    for (position in positions) {
        onView(withId(recyclerId)).perform(scrollToPosition(position))

        for (pair in imageUrls[position].withIndex()) {
            val nestedPosition = pair.index
            val url = pair.value

            imageLinkAction(position, nestedPosition, scrollTo())
            onView(withNestedViewHolder(recyclerId, nestedRecyclerId, position, nestedPosition))
                .check(matches(allOf(isDisplayed(), hasDescendant(withText(url)))))
        }
    }
}

/**
 * Wrapper function to take action on a link in an item in the nested RecyclerView
 *
 * @param position [Int]: position in main RecyclerView
 * @param nestedPosition [Int]: position in nested RecyclerView
 * @param action [ViewAction]: action to perform
 */
private fun imageLinkAction(position: Int, nestedPosition: Int, action: ViewAction) {
    onView(withId(recyclerId)).perform(
        actionOnNestedItemViewAtPosition(
            viewId = R.id.link,
            nestedRecyclerId = R.id.imagesRecycler,
            recyclerPosition = position,
            nestedViewPosition = nestedPosition,
            viewAction = action
        )
    )
}
