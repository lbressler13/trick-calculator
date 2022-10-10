package xyz.lbres.trickcalculator.helpers.matchers

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Created by dannyroa on 5/10/15.
 */
class RecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPosition(position: Int): TypeSafeMatcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            var resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description) {
                var idInfo = recyclerViewId.toString()
                if (resources != null) {
                    idInfo = try {
                        resources!!.getResourceName(recyclerViewId)
                    } catch (_: Resources.NotFoundException) {
                        "%d (resource name not found)".format(recyclerViewId)
                    }
                }

                description.appendText("with id: $idInfo")
            }

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
    }
}
