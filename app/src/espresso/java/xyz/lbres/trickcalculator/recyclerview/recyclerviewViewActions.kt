package xyz.lbres.trickcalculator.recyclerview

import androidx.annotation.IdRes
import androidx.test.espresso.ViewAction

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
