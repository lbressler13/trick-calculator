package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.helpers.actionOnItemViewAtPosition
import xyz.lbres.trickcalculator.helpers.actionOnNestedItemViewAtPosition
import xyz.lbres.trickcalculator.helpers.assertLinkOpened
import xyz.lbres.trickcalculator.helpers.clickLinkInText
import xyz.lbres.trickcalculator.helpers.notPresented
import xyz.lbres.trickcalculator.helpers.withNestedRecyclerView
import xyz.lbres.trickcalculator.helpers.withRecyclerView
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions

private val authorTitles =
    authorAttributions.map { "Icon made by ${it.name} from www.flaticon.com" }
private val imageUrls = authorAttributions.map { it.images.map { it.url } }
private const val recyclerId = R.id.attributionsRecycler
private const val nestedRecyclerId = R.id.imagesRecycler

fun testExpandCollapseAttributions() {
    onView(withRecyclerView(recyclerId).atPosition(0))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(authorTitles[0])))))
    onView(withRecyclerView(recyclerId).atPosition(1))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(authorTitles[1])))))
    onView(withRecyclerView(recyclerId).atPosition(2))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(authorTitles[2])))))
    onView(withRecyclerView(recyclerId).atPosition(3))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(authorTitles[3])))))
    onView(withRecyclerView(recyclerId).atPosition(4))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(authorTitles[4])))))

    for (url in imageUrls.flatten()) {
        onView(withText(url)).check(doesNotExist())
    }

    // expand all
    expandCollapseAttribution(0)
    checkImagesDisplayed(listOf(0))
    checkImagesDoNotExist(listOf(1, 2, 3, 4))

    expandCollapseAttribution(1)
    checkImagesDisplayed(listOf(0, 1))
    checkImagesDoNotExist(listOf(2, 3, 4))

    expandCollapseAttribution(2)
    checkImagesDisplayed(listOf(0, 1, 2))
    checkImagesDoNotExist(listOf(3, 4))

    expandCollapseAttribution(3)
    checkImagesDisplayed(listOf(0, 1, 2, 3))
    checkImagesDoNotExist(listOf(4))

    expandCollapseAttribution(4)
    checkImagesDisplayed(listOf(0, 1, 2, 3, 4))

    // collapse
    expandCollapseAttribution(3)
    checkImagesDisplayed(listOf(0, 1, 2, 4))
    checkImagesDoNotExist(listOf(3))

    expandCollapseAttribution(1)
    checkImagesDisplayed(listOf(0, 2, 4))
    checkImagesDoNotExist(listOf(1, 3))

    expandCollapseAttribution(0)
    checkImagesDisplayed(listOf(2, 4))
    checkImagesDoNotExist(listOf(0, 1, 3))

    expandCollapseAttribution(4)
    checkImagesDisplayed(listOf(2))
    checkImagesDoNotExist(listOf(0, 1, 3, 4))

    expandCollapseAttribution(2)
    checkImagesDoNotExist(listOf(0, 1, 2, 3, 4))
}

private fun checkImagesDoNotExist(positions: IntList) {
    for (position in positions) {
        for (url in imageUrls[position]) {
            onView(withText(url)).check(notPresented())
        }
    }
}

private fun expandCollapseAttribution(position: Int) {
    onView(withId(recyclerId)).perform(
        actionOnItemViewAtPosition(position, R.id.expandCollapseButton, click())
    )
}

private fun checkImagesDisplayed(positions: IntList) {
    for (position in positions) {
        for (pair in imageUrls[position].withIndex()) {
            val nestedPosition = pair.index
            val url = pair.value

            nestedItemAction(position, nestedPosition, scrollTo())
            onView(
                withNestedRecyclerView(recyclerId, nestedRecyclerId).atNestedPosition(
                    position,
                    nestedPosition
                )
            ).check(matches(allOf(isDisplayed(), hasDescendant(withText(url)))))
        }
    }
}

private fun nestedItemAction(position: Int, nestedPosition: Int, action: ViewAction) {
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

fun testAttributionLinks() {
    var expectedLinkClicks = 0

    // authors
    for (pair in authorAttributions.withIndex()) {
        val position = pair.index
        val author = pair.value

        // flaticon link
        onView(withId(recyclerId)).perform(
            actionOnItemViewAtPosition(position, R.id.attribution, clickLinkInText("www.flaticon.com"))
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
        val urls = imageUrls[position]

        for (pair in urls.withIndex()) {
            val nestedPosition = pair.index
            val url = pair.value

            nestedItemAction(position, nestedPosition, scrollTo())
            nestedItemAction(position, nestedPosition, clickLinkInText(url))
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
