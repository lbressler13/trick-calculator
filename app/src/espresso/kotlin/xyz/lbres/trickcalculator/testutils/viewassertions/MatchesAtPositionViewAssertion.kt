package xyz.lbres.trickcalculator.testutils.viewassertions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher
import xyz.lbres.kotlinutils.generic.ext.ifNull

private class MatchesAtPositionViewAssertion : ViewAssertion {
    private val assertion: ViewAssertion?
    private val matcher: Matcher<View?>?
    private val position: Int

    constructor(position: Int, matcher: Matcher<View?>) {
        this.position = position
        this.matcher = matcher

        assertion = null
    }

    constructor(position: Int, assertion: ViewAssertion) {
        this.position = position
        this.assertion = assertion

        matcher = null
    }

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (view !is RecyclerView) {
            throw AssertionError("View is not a RecyclerView: ${HumanReadables.describe(view)}")
        }

        val noMatchingViewException = AssertionError("No matching view found at position $position. Parent: ${HumanReadables.describe(view)}")

        val viewholder: View? = view.findViewHolderForAdapterPosition(position)?.itemView.ifNull { throw noMatchingViewException }
        if (matcher != null && !matcher.matches(viewholder)) {
            throw noMatchingViewException
        } else if (assertion != null) {
            assertion.check(view, noViewFoundException)
        }
    }
}

fun matchesAtPosition(position: Int, matcher: Matcher<View?>): ViewAssertion {
    return MatchesAtPositionViewAssertion(position, matcher)
}

fun matchesAtPosition(position: Int, matcher: ViewAssertion): ViewAssertion {
    return MatchesAtPositionViewAssertion(position, matcher)
}
