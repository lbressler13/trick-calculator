package xyz.lbres.trickcalculator.testutils.matchers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * [Matcher] to apply [vhMatcher] to a ViewHolder in a RecyclerView, at the given position
 *
 * @param position [Int]: position of ViewHolder in recycler
 * @param vhMatcher [Int]: matcher to apply to ViewHolder
 */
private class MatchesAtPositionViewMatcher(
    private val position: Int,
    private val vhMatcher: Matcher<View?>,
) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        if (description != null) {
            description.appendText("matching view at position $position with child matcher: ")
            description.appendDescriptionOf(vhMatcher)
        }
    }

    /**
     * Applies matcher to ViewHolder at index [position] in the recycler
     *
     * @param view [View]?: view to check, expected to be a [RecyclerView]
     * @return [Boolean] `true` if the ViewHolder exists and matches the matcher, `false` otherwise
     */
    override fun matchesSafely(view: View?): Boolean {
        if (view !is RecyclerView) {
            return false
        }

        val viewholder: View? = view.findViewHolderForAdapterPosition(position)?.itemView
        if (viewholder == null) {
            return false
        }

        return vhMatcher.matches(viewholder)
    }
}

/**
 * [Matcher] for a RecyclerView, to determine if the ViewHolder at a specified position matches a given matcher
 *
 * @param position [Int]: position of ViewHolder in recycler
 * @param vhMatcher [Int]: matcher for ViewHolder
 */
fun matchesAtPosition(position: Int, vhMatcher: Matcher<View?>): Matcher<View> {
    return MatchesAtPositionViewMatcher(position, vhMatcher)
}
