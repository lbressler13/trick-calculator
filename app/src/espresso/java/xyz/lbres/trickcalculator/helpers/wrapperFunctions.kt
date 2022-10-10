package xyz.lbres.trickcalculator.helpers

import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.MatcherAssert.assertThat
import xyz.lbres.trickcalculator.helpers.matchers.NestedRecyclerViewMatcher
import xyz.lbres.trickcalculator.helpers.matchers.RecyclerViewMatcher
import xyz.lbres.trickcalculator.helpers.viewactions.ActionOnItemViewAtPositionViewAction
import xyz.lbres.trickcalculator.helpers.viewactions.ActionOnNestedItemViewAtPositionViewAction
import xyz.lbres.trickcalculator.helpers.viewactions.ClickLinkInTextViewAction
import xyz.lbres.trickcalculator.helpers.viewactions.ClickViewViewAction

fun actionOnItemViewAtPosition(
    position: Int,
    @IdRes viewId: Int,
    viewAction: ViewAction
): ViewAction {
    return ActionOnItemViewAtPositionViewAction(
        position,
        viewId,
        viewAction
    )
}

fun actionOnNestedItemViewAtPosition(
    recyclerPosition: Int,
    nestedViewPosition: Int,
    @IdRes nestedRecyclerId: Int,
    @IdRes viewId: Int,
    viewAction: ViewAction
): ViewAction {
    return ActionOnNestedItemViewAtPositionViewAction(
        recyclerPosition,
        nestedViewPosition,
        nestedRecyclerId,
        viewId,
        viewAction
    )
}

fun withRecyclerView(id: Int) = RecyclerViewMatcher(id)

fun withNestedRecyclerView(id: Int, nestedId: Int) = NestedRecyclerViewMatcher(id, nestedId)

fun clickLinkInText(textToClick: String) = ClickLinkInTextViewAction(textToClick)

fun clickView() = ClickViewViewAction()

// adapted from responses on this StackOverflow post: https://stackoverflow.com/questions/41297524/espresso-check-view-either-doesnotexist-or-not-isdisplayed
fun notPresented(): ViewAssertion {
    return ViewAssertion { view, _ ->
        if (view != null && !view.isVisible) {
            assertThat("View is present in hierarchy and visible: ${HumanReadables.describe(view)}", true)
        }
    }
}
