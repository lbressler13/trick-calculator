package xyz.lbres.trickcalculator.testutils.matchers

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

private class WithMatchingChildAtPositionViewMatcher(private val recyclerViewId: Int, private val position: Int, private val childViewId: Int): TypeSafeMatcher<View>() {
    private var resources: Resources? = null
    private var viewAtPosition: View? = null

    override fun describeTo(description: Description?) {
        // TODO
    }

    override fun matchesSafely(view: View): Boolean {
        if (viewAtPosition != null) {
            return view == viewAtPosition
        }

        resources = view.resources
        val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
        if (recyclerView == null || recyclerView.id != recyclerViewId) {
            return false
        }

        viewAtPosition = recyclerView.findViewHolderForAdapterPosition(position)?.itemView
        if (view != viewAtPosition) {
            return false
        }

        return view.findViewById<View>(childViewId) != null
    }
}

fun withMatchingChildAtPosition(recyclerViewId: Int, position: Int, childViewId: Int): Matcher<View> {
    return WithMatchingChildAtPositionViewMatcher(recyclerViewId, position, childViewId)
}
