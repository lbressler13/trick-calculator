package xyz.lbres.trickcalculator.helpers.matchers

import android.content.res.Resources
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * [TypeSafeMatcher] similar to [RecyclerViewMatcher], using nested RecyclerViews.
 * The positions in the outer RecyclerView and nested RecyclerView are both passed as parameters,
 * and the view in the nested RecyclerView is matched.
 *
 * @param recyclerViewId [IdRes]: view ID of outer RecyclerView
 * @param nestedRecyclerViewId [IdRes]: view ID of nested RecyclerView
 * @param position [Int]: position of first ViewHolder in outer RecyclerView
 * @param nestedPosition [Int]: position of ViewHolder in nested RecyclerView
 */
class NestedRecyclerViewMatcher(
    @param:IdRes private val recyclerViewId: Int,
    @param:IdRes private val nestedRecyclerViewId: Int,
    private val position: Int,
    private val nestedPosition: Int
) : TypeSafeMatcher<View?>() {
    var resources: Resources? = null
    var childView: View? = null

    override fun describeTo(description: Description) {
        var idInfo = "$recyclerViewId, $nestedRecyclerViewId"

        if (resources != null) {
            val recyclerName = try {
                resources!!.getResourceName(recyclerViewId)
            } catch (_: Resources.NotFoundException) {
                "$recyclerViewId (resource name not found)".format(recyclerViewId)
            }

            val nestedRecyclerName = try {
                resources!!.getResourceName(nestedRecyclerViewId)
            } catch (_: Resources.NotFoundException) {
                "$nestedRecyclerViewId (resource name not found)"
            }

            idInfo = "$recyclerName, $nestedRecyclerName"
        }
        description.appendText("with ids: $idInfo")
    }

    /**
     * Matches item at index [position] in the outer RecyclerView and [nestedPosition] in the nested RecyclerView
     *
     * @param view [View]?: view to check
     * @return [Boolean] true if view is at index [nestedPosition] in the RecyclerView with ID [nestedRecyclerViewId],
     * which is contained in the ViewHolder at index [position] in the RecyclerView with ID [recyclerViewId], false otherwise
     */
    override fun matchesSafely(view: View?): Boolean {
        return when {
            view == null -> false
            childView != null -> view == childView
            else -> {
                var viewMatched = false
                resources = view.resources

                val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                if (recyclerView != null && recyclerView.id == recyclerViewId) {
                    val viewholder =
                        recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                    val nestedRecycler = viewholder.findViewById<RecyclerView>(nestedRecyclerViewId)

                    if (nestedRecycler != null && nestedRecycler.id == nestedRecyclerViewId) {
                        childView =
                            nestedRecycler.findViewHolderForAdapterPosition(nestedPosition)!!.itemView
                        viewMatched = view == childView
                    }
                }

                viewMatched
            }
        }
    }
}
