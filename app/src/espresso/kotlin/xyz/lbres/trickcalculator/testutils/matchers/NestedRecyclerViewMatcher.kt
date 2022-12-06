package xyz.lbres.trickcalculator.testutils.matchers

import android.content.res.Resources
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * [TypeSafeMatcher] similar to [RecyclerViewMatcher], using nested RecyclerViews.
 * The positions in the outer RecyclerView and nested RecyclerView are both passed as parameters,
 * and the view in the nested RecyclerView is matched.
 *
 * @param recyclerViewId [IdRes]: view ID of outer RecyclerView
 * @param nestedRecyclerViewId [IdRes]: view ID of nested RecyclerView
 * @param recyclerPosition [Int]: position of first ViewHolder in outer RecyclerView
 * @param nestedViewPosition [Int]: position of ViewHolder in nested RecyclerView
 */
private class NestedRecyclerViewMatcher(
    @param:IdRes private val recyclerViewId: Int,
    @param:IdRes private val nestedRecyclerViewId: Int,
    private val recyclerPosition: Int,
    private val nestedViewPosition: Int
) : TypeSafeMatcher<View>() {
    private var resources: Resources? = null
    private var childView: View? = null

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
     * Matches item at index [recyclerPosition] in the outer RecyclerView and [nestedViewPosition] in the nested RecyclerView
     *
     * @param view [View]: view to check
     * @return [Boolean] true if view is at index [nestedViewPosition] in the RecyclerView with ID [nestedRecyclerViewId],
     * which is contained in the ViewHolder at index [recyclerPosition] in the RecyclerView with ID [recyclerViewId], false otherwise
     */
    override fun matchesSafely(view: View): Boolean {
        if (childView != null) {
            return view == childView
        }

        resources = view.resources

        val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
        if (recyclerView != null && recyclerView.id == recyclerViewId) {
            val viewholder = recyclerView.findViewHolderForAdapterPosition(recyclerPosition)!!.itemView

            val nestedRecycler = viewholder.findViewById<RecyclerView>(nestedRecyclerViewId)

            if (nestedRecycler != null && nestedRecycler.id == nestedRecyclerViewId) {
                childView = nestedRecycler.findViewHolderForAdapterPosition(nestedViewPosition)!!.itemView
                return view == childView
            }
        }

        return false
    }
}

/**
 * Wrapper function for creating a [NestedRecyclerViewMatcher]
 */
fun withNestedViewHolder(
    @IdRes recyclerId: Int,
    @IdRes nestedRecyclerId: Int,
    recyclerPosition: Int,
    nestedViewPosition: Int
): Matcher<View> {
    return NestedRecyclerViewMatcher(recyclerId, nestedRecyclerId, recyclerPosition, nestedViewPosition)
}
