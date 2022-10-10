package xyz.lbres.trickcalculator.helpers.matchers

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class NestedRecyclerViewMatcher(
    private val recyclerViewId: Int,
    private val nestedRecyclerViewId: Int
) {
    fun atNestedPosition(position: Int, nestedPosition: Int): TypeSafeMatcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            var resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description) {
                var idInfo = "$recyclerViewId, $nestedRecyclerViewId"

                if (resources != null) {
                    val recyclerName = try {
                        resources!!.getResourceName(recyclerViewId)
                    } catch (_: Resources.NotFoundException) {
                        "%d (resource name not found)".format(recyclerViewId)
                    }

                    val nestedRecyclerName = try {
                        resources!!.getResourceName(nestedRecyclerViewId)
                    } catch (_: Resources.NotFoundException) {
                        "%d (resource name not found)".format(nestedRecyclerViewId)
                    }

                    idInfo = "$recyclerName, $nestedRecyclerName"
                }
                description.appendText("with ids: $idInfo")
            }

            override fun matchesSafely(view: View?): Boolean {
                return when {
                    view == null -> false
                    childView != null -> view == childView
                    else -> {
                        var viewMatched = false
                        resources = view.resources

                        val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                        if (recyclerView != null && recyclerView.id == recyclerViewId) {
                            val viewholder = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                            val nestedRecycler = viewholder.findViewById<RecyclerView>(nestedRecyclerViewId)

                            if (nestedRecycler != null && nestedRecycler.id == nestedRecyclerViewId) {
                                childView = nestedRecycler.findViewHolderForAdapterPosition(nestedPosition)!!.itemView
                                viewMatched = view == childView
                            }
                        }

                        viewMatched
                    }
                }
            }
        }
    }
}
