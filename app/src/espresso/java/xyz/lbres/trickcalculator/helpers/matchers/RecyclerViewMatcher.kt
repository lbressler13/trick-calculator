package xyz.lbres.trickcalculator.helpers.matchers

import android.content.res.Resources
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * [TypeSafeMatcher] to match on a view at a specific position in a RecyclerView.
 *
 * Adapted from similar Matcher created by GitHub user dannyroa in this file:
 * https://github.com/dannyroa/espresso-samples/blob/master/RecyclerView/app/src/androidTest/java/com/dannyroa/espresso_samples/recyclerview/RecyclerViewMatcher.java
 *
 * @param recyclerViewId [IdRes]: view ID of ReyclerView
 * @param position [Int]: position in recycler to match to
 */
class RecyclerViewMatcher(@param:IdRes private val recyclerViewId: Int, private val position: Int) :
    TypeSafeMatcher<View?>() {
    var resources: Resources? = null
    var childView: View? = null

    override fun describeTo(description: Description) {
        var idInfo = recyclerViewId.toString()
        if (resources != null) {
            idInfo = try {
                resources!!.getResourceName(recyclerViewId)
            } catch (_: Resources.NotFoundException) {
                "$recyclerViewId (resource name not found)"
            }
        }

        description.appendText("with id: $idInfo")
    }

    /**
     * Matches item at index [position] in the recycler
     *
     * @param view [View]?: view to check
     * @return [Boolean] true if view is at index [position] in the RecyclerView with ID [recyclerViewId], false otherwise
     */
    override fun matchesSafely(view: View?): Boolean {
        return when {
            view == null -> false
            childView != null -> view == childView
            else -> {
                resources = view.resources
                val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                if (recyclerView != null && recyclerView.id == recyclerViewId) {
                    childView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                    view == childView
                } else {
                    false
                }
            }
        }
    }
}
