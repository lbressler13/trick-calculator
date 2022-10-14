package xyz.lbres.trickcalculator.helpers

import androidx.annotation.IdRes
import androidx.test.espresso.ViewAction
import xyz.lbres.trickcalculator.helpers.matchers.NestedRecyclerViewMatcher
import xyz.lbres.trickcalculator.helpers.matchers.RecyclerViewMatcher
import xyz.lbres.trickcalculator.helpers.viewaction.ActionOnItemViewAtPositionViewAction
import xyz.lbres.trickcalculator.helpers.viewaction.ActionOnNestedItemViewAtPositionViewAction
import xyz.lbres.trickcalculator.helpers.viewaction.ClickLinkInTextViewAction
import xyz.lbres.trickcalculator.helpers.viewaction.ForceClickViewAction
import xyz.lbres.trickcalculator.helpers.viewassertion.NotPresentedViewAssertion

/**
 * VIEW ACTIONS
 */

/**
 * Wrapper function for creating an [ActionOnItemViewAtPositionViewAction]
 */
fun actionOnItemViewAtPosition(position: Int, @IdRes viewId: Int, viewAction: ViewAction) =
    ActionOnItemViewAtPositionViewAction(position, viewId, viewAction)

/**
 * Wrapper function for creating an [ActionOnNestedItemViewAtPositionViewAction]
 */
fun actionOnNestedItemViewAtPosition(
    recyclerPosition: Int,
    nestedViewPosition: Int,
    @IdRes nestedRecyclerId: Int,
    @IdRes viewId: Int,
    viewAction: ViewAction
): ActionOnNestedItemViewAtPositionViewAction {
    return ActionOnNestedItemViewAtPositionViewAction(
        recyclerPosition,
        nestedViewPosition,
        nestedRecyclerId,
        viewId,
        viewAction
    )
}

/**
 * Wrapper function for creating a [ClickLinkInTextViewAction]
 */
fun clickLinkInText(textToClick: String) = ClickLinkInTextViewAction(textToClick)

/**
 * Wrapper function for creating a [ForceClickViewAction]
 */
fun forceClick() = ForceClickViewAction()

/**
 * VIEW MATCHERS
 */

/**
 * Wrapper function for creating a [RecyclerViewMatcher]
 */
fun withViewHolder(@IdRes recyclerId: Int, position: Int) =
    RecyclerViewMatcher(recyclerId, position)

/**
 * Wrapper function for creating a [NestedRecyclerViewMatcher]
 */
fun withNestedViewHolder(
    @IdRes recyclerId: Int,
    @IdRes nestedRecyclerId: Int,
    position: Int,
    nestedPosition: Int
): NestedRecyclerViewMatcher {
    return NestedRecyclerViewMatcher(recyclerId, nestedRecyclerId, position, nestedPosition)
}

/**
 * VIEW ASSERTIONS
 */

/**
 * Wrapper function for creating a [NotPresentedViewAssertion]
 */
fun isNotPresented() = NotPresentedViewAssertion()
