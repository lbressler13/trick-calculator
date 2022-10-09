package xyz.lbres.trickcalculator.ui.attributions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.ui.attributions.authorattribution.AuthorAttributionViewHolder
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions
import xyz.lbres.trickcalculator.ui.attributions.imageattribution.ImageAttributionViewHolder

class AuthorAttributionTestItem(position: Int) {
    private val name = authorAttributions[position].name
    val title: String = "Icon made by $name from www.flaticon.com"
    val imageTitles: StringList = authorAttributions[position].images.map { it.url }
    private var imagesRecycler: RecyclerView? = null
    private val itemMatcher: Matcher<View>

    init {
        itemMatcher = object : BaseMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.appendText("Matching to recycler for test item at position $position")
            }

            override fun matches(item: Any?): Boolean {
                return item != null && item is AuthorAttributionViewHolder && item.binding.imagesRecycler == imagesRecycler
            }
        }
    }

    fun setImagesRecycler(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = Matchers.instanceOf(AuthorAttributionViewHolder::class.java)
            override fun getDescription(): String = "Storing images recycler"

            override fun perform(uiController: UiController?, view: View?) {
                imagesRecycler = view?.findViewById(R.id.imagesRecycler)
            }
        }
    }

    fun expandCollapse(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = Matchers.instanceOf(AuthorAttributionViewHolder::class.java)
            override fun getDescription(): String = "Expanding/collapsing an author attribution"

            override fun perform(uiController: UiController?, view: View?) {
                val button = view?.findViewById<View>(R.id.expandCollapseButton)
                button?.callOnClick()
            }
        }
    }

    fun checkImagesDisplayed() {
        val firstChild = imageTitles[0]
        // val recycler = onView(allOf(withId(imagesRecycler!!.id), hasDescendant(withText(firstChild))))
        // val recycler = onView(allOf(withId(imagesRecycler!!.id), hasChildCount(imageTitles.size)))
        val recycler = onView(allOf(withId(imagesRecycler!!.id), hasChildCount(imageTitles.size)))

        for (pair in imageTitles.withIndex()) {
            val index = pair.index
            val rowTitle = pair.value

            recycler.perform(
                RecyclerViewActions.scrollToPosition<ImageAttributionViewHolder>(index)
            )
            onView(withText(rowTitle)).check(matches(isDisplayed()))
        }
    }
}
