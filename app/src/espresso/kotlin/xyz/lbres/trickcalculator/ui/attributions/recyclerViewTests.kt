package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.assertLinkOpened
import xyz.lbres.trickcalculator.testutils.viewactions.clickLinkInText
import xyz.lbres.trickcalculator.testutils.viewactions.clickLinkInTextWithViewId
import xyz.lbres.trickcalculator.testutils.viewassertions.matchesAtPosition
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions

private val imageUrls = authorAttributions.map { it.images.map { it.url } }
private val attributionsRecycler = onView(withId(R.id.attributionsRecycler))

fun testExpandCollapseAttributions() {
    val authorTitles = authorAttributions.map { "Icon made by ${it.name} from www.flaticon.com" }
    val titleAssertion: (Int) -> ViewAssertion = {
        matchesAtPosition(it, allOf(isDisplayed(), hasDescendant(withText(authorTitles[it]))))
    }

    attributionsRecycler.check(titleAssertion(0))
    attributionsRecycler.check(titleAssertion(1))
    attributionsRecycler.check(titleAssertion(2))
    attributionsRecycler.check(titleAssertion(3))
    attributionsRecycler.check(titleAssertion(4))

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
    val clickFlaticon = clickLinkInTextWithViewId(R.id.attribution, "www.flaticon.com")

    // authors
    for (pair in authorAttributions.withIndex()) {
        val position = pair.index
        val author = pair.value

        // flaticon link
        attributionsRecycler.perform(actionOnAuthorItemAtPosition(position, clickFlaticon))
        expectedLinkClicks++
        assertLinkOpened("https://www.flaticon.com", expectedLinkClicks)

        // author link
        val clickAuthor = clickLinkInTextWithViewId(R.id.attribution, author.name)
        attributionsRecycler.perform(actionOnAuthorItemAtPosition(position, clickAuthor))
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
        attributionsRecycler.perform(scrollToAuthorPosition(position))
        val urls = imageUrls[position]

        for (url in urls) {
            onView(withText(containsString(url))).perform(clickLinkInText(url))
            expectedLinkClicks++
            assertLinkOpened(url, expectedLinkClicks)
        }
    }

    // check link with attributions open
    attributionsRecycler.perform(actionOnAuthorItemAtPosition(0, clickFlaticon))
    expectedLinkClicks++
    assertLinkOpened("https://www.flaticon.com", expectedLinkClicks)

    // author link
    val author = authorAttributions[0]
    val clickAuthor = clickLinkInTextWithViewId(R.id.attribution, author.name)
    attributionsRecycler.perform(actionOnAuthorItemAtPosition(0, clickAuthor))
    expectedLinkClicks++
    assertLinkOpened(author.url, expectedLinkClicks)
}
