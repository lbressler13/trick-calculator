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
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.recyclerview.actionOnItemViewAtPosition
import xyz.lbres.trickcalculator.recyclerview.actionOnNestedItemViewAtPosition
import xyz.lbres.trickcalculator.recyclerview.withNestedRecyclerView
import xyz.lbres.trickcalculator.recyclerview.withRecyclerView
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

    expandCollapseAttribution(0)
    checkImagesDisplayed(0)

    expandCollapseAttribution(1)
    checkImagesDisplayed(0)
    checkImagesDisplayed(1)

    expandCollapseAttribution(2)
    checkImagesDisplayed(0)
    checkImagesDisplayed(1)
    checkImagesDisplayed(2)

    expandCollapseAttribution(3)
    checkImagesDisplayed(0)
    checkImagesDisplayed(1)
    checkImagesDisplayed(2)
    checkImagesDisplayed(3)

    expandCollapseAttribution(4)
    checkImagesDisplayed(0)
    checkImagesDisplayed(1)
    checkImagesDisplayed(2)
    checkImagesDisplayed(3)
    checkImagesDisplayed(4)
}

private fun expandCollapseAttribution(position: Int) {
    onView(withId(recyclerId)).perform(
        actionOnItemViewAtPosition(position, R.id.expandCollapseButton, click())
    )
}

private fun checkImagesDisplayed(position: Int) {
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

private fun checkImagesNotDisplayed(position: Int) {
    // TODO
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
    // TODO
}
