package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.matchers.matchesAtPosition
import xyz.lbres.trickcalculator.testutils.viewactions.actionOnChildWithId
import xyz.lbres.trickcalculator.testutils.viewactions.clickChildWithId
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented
import xyz.lbres.trickcalculator.ui.attributions.authorattribution.AuthorAttributionViewHolder
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions
import xyz.lbres.trickcalculator.ui.attributions.imageattribution.ImageAttributionViewHolder

private val imageUrls = authorAttributions.map { it.images.map { it.url } }
private const val recyclerId = R.id.attributionsRecycler
private const val nestedRecyclerId = R.id.imagesRecycler

fun scrollToAuthorPosition(position: Int): ViewAction {
    return RecyclerViewActions.scrollToPosition<AuthorAttributionViewHolder>(position)
}

fun scrollToImagePosition(position: Int): ViewAction {
    return RecyclerViewActions.scrollToPosition<ImageAttributionViewHolder>(position)
}

fun actionOnAuthorItemAtPosition(position: Int, action: ViewAction): ViewAction {
    return RecyclerViewActions.actionOnItemAtPosition<AuthorAttributionViewHolder>(position, action)
}

fun actionOnImageItemAtPosition(position: Int, action: ViewAction): ViewAction {
    return RecyclerViewActions.actionOnItemAtPosition<ImageAttributionViewHolder>(position, action)
}

/**
 * Click the expand/collapse button for an element in the main RecyclerView
 *
 * @param position [Int]: position of item to click
 */
fun expandCollapseAttribution(position: Int) {
    val action = clickChildWithId(R.id.expandCollapseButton)
    onView(withId(recyclerId)).perform(actionOnAuthorItemAtPosition(position, action))
    onView(withId(recyclerId)).check(matches(matchesAtPosition(position, hasDescendant(withId(nestedRecyclerId)))))
}

/**
 * Check that image urls for specified dropdowns are not visible
 *
 * @param positions [IntList]: list of positions where image urls should not be visible
 */
fun checkImagesNotPresented(positions: IntList) {
    for (position in positions) {
        onView(withId(recyclerId)).perform(scrollToAuthorPosition(0))
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
        onView(withId(recyclerId)).perform(scrollToAuthorPosition(position))

        for (pair in imageUrls[position].withIndex()) {
            val nestedPosition = pair.index
            val url = pair.value

            scrollToLink(position, nestedPosition)

            val urlMatcher = allOf(isDisplayed(), hasDescendant(withText(url)))
            val nestedMatcher = allOf(withId(nestedRecyclerId), isDisplayed(), matchesAtPosition(nestedPosition, urlMatcher))

            onView(withId(recyclerId)).check(matches(matchesAtPosition(position, hasDescendant(nestedMatcher))))
        }
    }
}

/**
 * Wrapper function to take action on a link in an item in the nested RecyclerView
 *
 * @param position [Int]: position in main RecyclerView
 * @param nestedPosition [Int]: position in nested RecyclerView
 */
private fun scrollToLink(position: Int, nestedPosition: Int) {
    val childAction = actionOnChildWithId(R.id.imagesRecycler, scrollToImagePosition(nestedPosition))
    onView(withId(recyclerId)).perform(actionOnAuthorItemAtPosition(position, childAction))
}
