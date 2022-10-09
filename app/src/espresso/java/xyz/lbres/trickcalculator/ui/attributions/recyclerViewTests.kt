package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.allOf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.ui.attributions.authorattribution.AuthorAttributionViewHolder
import xyz.lbres.trickcalculator.ui.attributions.imageattribution.ImageAttributionViewHolder

private val testItems = List(5) { AuthorAttributionTestItem(it) }
private val recyclerId = R.id.attributionsRecycler

fun testExpandCollapseAttributions() {
    onView(withRecyclerView(recyclerId).atPosition(0))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(testItems[0].title)))))
    onView(withRecyclerView(recyclerId).atPosition(1))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(testItems[1].title)))))
    onView(withRecyclerView(recyclerId).atPosition(2))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(testItems[2].title)))))
    onView(withRecyclerView(recyclerId).atPosition(3))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(testItems[3].title)))))
    onView(withRecyclerView(recyclerId).atPosition(4))
        .check(matches(allOf(isDisplayed(), hasDescendant(withText(testItems[4].title)))))

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
    for (nestedPosition in testItems[position].imageTitles.indices) {
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
