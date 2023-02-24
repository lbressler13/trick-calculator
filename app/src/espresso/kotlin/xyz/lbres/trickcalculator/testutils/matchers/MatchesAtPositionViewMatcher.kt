package xyz.lbres.trickcalculator.testutils.matchers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * [Matcher] for applying checks to a ViewHolder with given traits in a RecyclerView
 *
 * @param position [Int]: position of ViewHolder in recycler
 * @param matcher [Int]: matcher to apply to ViewHolder
 */
private class MatchesAtPositionViewMatcher(private val position: Int, private val matcher: Matcher<View?>) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        if (description != null) {
            description.appendText("matching view at position $position with child matcher: ")
            description.appendDescriptionOf(matcher)
        }
    }

    /**
     * Applies matcher to ViewHolder at index [position] in the recycler
     *
     * @param view [View]: view to check
     * @return [Boolean] true if view is at index [position] in the RecyclerView with ID [recyclerViewId], false otherwise
     */
    override fun matchesSafely(view: View?): Boolean {
        if (view !is RecyclerView) {
            return false
        }

        val viewholder: View? = view.findViewHolderForAdapterPosition(position)?.itemView
        if (viewholder == null) {
            return false
        }

        return matcher.matches(viewholder)
    }
}

fun matchesAtPosition(position: Int, matcher: Matcher<View?>): Matcher<View> = MatchesAtPositionViewMatcher(position, matcher)
