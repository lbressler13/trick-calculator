package xyz.lbres.trickcalculator.recyclerview

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAction

fun <VH : RecyclerView.ViewHolder?> actionOnItemViewAtPosition(
    position: Int,
    @IdRes viewId: Int,
    viewAction: ViewAction
): ViewAction {
    return ActionOnItemViewAtPositionViewAction<RecyclerView.ViewHolder?>(
        position,
        viewId,
        viewAction
    )
}

fun <VH : RecyclerView.ViewHolder?> actionOnNestedItemViewAtPosition(
    recyclerPosition: Int,
    nestedViewPosition: Int,
    @IdRes nestedRecyclerId: Int,
    @IdRes viewId: Int,
    viewAction: ViewAction
): ViewAction {
    return ActionOnNestedItemViewAtPositionViewAction<RecyclerView.ViewHolder?>(
        recyclerPosition,
        nestedViewPosition,
        nestedRecyclerId,
        viewId,
        viewAction
    )
}

fun withRecyclerView(id: Int) = RecyclerViewMatcher(id)
