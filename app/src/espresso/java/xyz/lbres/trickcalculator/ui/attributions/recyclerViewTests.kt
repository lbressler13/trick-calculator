package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.allOf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.recyclerview.actionOnItemViewAtPosition
import xyz.lbres.trickcalculator.recyclerview.actionOnNestedItemViewAtPosition
import xyz.lbres.trickcalculator.recyclerview.withRecyclerView
import xyz.lbres.trickcalculator.ui.attributions.authorattribution.AuthorAttributionViewHolder
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions

private val authorTitles = authorAttributions.map { "Icon made by ${it.name} from www.flaticon.com" }
private val imageUrls = authorAttributions.map { it.images.map { it.url } }
private const val recyclerId = R.id.attributionsRecycler

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
}

private fun expandCollapseAttribution(position: Int) {
    onView(withId(recyclerId)).perform(
        actionOnItemViewAtPosition<AuthorAttributionViewHolder>(
            position,
            R.id.expandCollapseButton,
            click()
        )
    )
}

private fun checkImagesDisplayed(position: Int) {
    for (nestedPosition in imageUrls.indices) {
        onView(withId(recyclerId)).perform(
            actionOnNestedItemViewAtPosition<AuthorAttributionViewHolder>(
                recyclerPosition = position,
                nestedViewPosition = nestedPosition,
                nestedRecyclerId = R.id.imagesRecycler,
                viewId = R.id.link,
                scrollTo()
            )
        )
    }
}

fun testAttributionLinks() {
    // TODO
}
