package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.helpers.actionOnItemViewAtPosition
import xyz.lbres.trickcalculator.helpers.actionOnNestedItemViewAtPosition
import xyz.lbres.trickcalculator.helpers.assertLinkOpened
import xyz.lbres.trickcalculator.helpers.clickLinkInText
import xyz.lbres.trickcalculator.helpers.isNotPresented
import xyz.lbres.trickcalculator.helpers.withNestedViewHolder
import xyz.lbres.trickcalculator.helpers.withViewHolder
import xyz.lbres.trickcalculator.ui.attributions.authorattribution.AuthorAttributionViewHolder
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions

private val imageUrls = authorAttributions.map { it.images.map { it.url } }
private const val recyclerId = R.id.attributionsRecycler
private const val nestedRecyclerId = R.id.imagesRecycler

fun testExpandCollapseAttributions() {
    val authorTitles = authorAttributions.map { "Icon made by ${it.name} from www.flaticon.com" }
    onView(withViewHolder(recyclerId, 0))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(authorTitles[0])))))
    onView(withViewHolder(recyclerId, 1))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(authorTitles[1])))))
    onView(withViewHolder(recyclerId, 2))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(authorTitles[2])))))
    onView(withViewHolder(recyclerId, 3))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(authorTitles[3])))))
    onView(withViewHolder(recyclerId, 4))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(authorTitles[4])))))

    checkImagesNotPresented(listOf(0, 1, 2, 3, 4))

    // expand all
    expandCollapseAttribution(0)
    checkImagesDisplayed(listOf(0))
    checkImagesNotPresented(listOf(1, 2, 3, 4))

    expandCollapseAttribution(1)
    checkImagesDisplayed(listOf(0, 1))
    checkImagesNotPresented(listOf(2, 3, 4))

    expandCollapseAttribution(2)
    checkImagesDisplayed(listOf(0, 1, 2))
    checkImagesNotPresented(listOf(3, 4))

    expandCollapseAttribution(3)
    checkImagesDisplayed(listOf(0, 1, 2, 3))
    checkImagesNotPresented(listOf(4))

    expandCollapseAttribution(4)
    checkImagesDisplayed(listOf(0, 1, 2, 3, 4))

    // collapse
    expandCollapseAttribution(3)
    checkImagesDisplayed(listOf(0, 1, 2, 4))
    checkImagesNotPresented(listOf(3))

    expandCollapseAttribution(1)
    checkImagesDisplayed(listOf(0, 2, 4))
    checkImagesNotPresented(listOf(1, 3))

    expandCollapseAttribution(0)
    checkImagesDisplayed(listOf(2, 4))
    checkImagesNotPresented(listOf(0, 1, 3))

    expandCollapseAttribution(4)
    checkImagesDisplayed(listOf(2))
    checkImagesNotPresented(listOf(0, 1, 3, 4))

    expandCollapseAttribution(2)
    checkImagesNotPresented(listOf(0, 1, 2, 3, 4))
}

fun testAttributionLinks() {
    var expectedLinkClicks = 0

    // authors
    for (pair in authorAttributions.withIndex()) {
        val position = pair.index
        val author = pair.value

        // flaticon link
        onView(withId(recyclerId)).perform(
            actionOnItemViewAtPosition(
                position,
                R.id.attribution,
                clickLinkInText("www.flaticon.com")
            )
        )
        expectedLinkClicks++
        assertLinkOpened("https://www.flaticon.com", expectedLinkClicks)

        // author link
        onView(withId(recyclerId)).perform(
            actionOnItemViewAtPosition(position, R.id.attribution, clickLinkInText(author.name))
        )
        expectedLinkClicks++
        assertLinkOpened(author.url, expectedLinkClicks)
    }

    // images
    expandCollapseAttribution(0)
    expandCollapseAttribution(1)
    expandCollapseAttribution(2)
    expandCollapseAttribution(3)
    expandCollapseAttribution(4)
    for (position in imageUrls.indices) {
        onView(withId(recyclerId)).perform(RecyclerViewActions.scrollToPosition<AuthorAttributionViewHolder>(position))
        val urls = imageUrls[position]

        for (url in urls) {
            onView(withText(containsString(url))).perform(clickLinkInText(url))
            expectedLinkClicks++
            assertLinkOpened(url, expectedLinkClicks)
        }
    }

    // check link with attributions open
    onView(withId(recyclerId)).perform(
        actionOnItemViewAtPosition(0, R.id.attribution, clickLinkInText("www.flaticon.com"))
    )
    expectedLinkClicks++
    assertLinkOpened("https://www.flaticon.com", expectedLinkClicks)

    // author link
    val author = authorAttributions[0]
    onView(withId(recyclerId)).perform(
        actionOnItemViewAtPosition(0, R.id.attribution, clickLinkInText(author.name))
    )
    expectedLinkClicks++
    assertLinkOpened(author.url, expectedLinkClicks)
}

/**
 * Check that image urls for specified dropdowns are not visible
 *
 * @param positions [IntList]: list of positions where image urls should not be visible
 */
private fun checkImagesNotPresented(positions: IntList) {
    for (position in positions) {
        for (url in imageUrls[position]) {
            onView(withText(url)).check(isNotPresented())
        }
    }
}

/**
 * Click the expand/collapse button for an element in the main RecyclerView
 *
 * @param position [Int]: position of item to click
 */
private fun expandCollapseAttribution(position: Int) {
    onView(withId(recyclerId)).perform(RecyclerViewActions.scrollToPosition<AuthorAttributionViewHolder>(0))
    onView(withId(recyclerId)).perform(
        actionOnItemViewAtPosition(position, R.id.expandCollapseButton, click())
    )
}

/**
 * Check that image urls for specified dropdowns are visible
 *
 * @param positions [IntList]: list of positions where image urls should be visible
 */
private fun checkImagesDisplayed(positions: IntList) {
    for (position in positions) {
        for (pair in imageUrls[position].withIndex()) {
            val nestedPosition = pair.index
            val url = pair.value

            imageLinkAction(position, nestedPosition, scrollTo())
            onView(
                withNestedViewHolder(recyclerId, nestedRecyclerId, position, nestedPosition)
            ).check(matches(allOf(isDisplayed(), hasDescendant(withText(url)))))
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
            recyclerPosition = position,
            nestedViewPosition = nestedPosition,
            nestedRecyclerId = R.id.imagesRecycler,
            viewId = R.id.link,
            action
        )
    )
}
