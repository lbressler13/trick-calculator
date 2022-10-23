package xyz.lbres.trickcalculator.testutils

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import xyz.lbres.trickcalculator.testutils.matchers.NestedRecyclerViewMatcher
import xyz.lbres.trickcalculator.testutils.matchers.RecyclerViewMatcher
import xyz.lbres.trickcalculator.testutils.viewactions.ActionOnItemViewAtPositionViewAction
import xyz.lbres.trickcalculator.testutils.viewactions.ActionOnNestedItemViewAtPositionViewAction
import xyz.lbres.trickcalculator.testutils.viewactions.ClickLinkInTextViewAction
import xyz.lbres.trickcalculator.testutils.viewactions.ForceClickViewAction
import xyz.lbres.trickcalculator.testutils.viewassertions.NotPresentedViewAssertion

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
): Matcher<View?> {
    return NestedRecyclerViewMatcher(recyclerId, nestedRecyclerId, position, nestedPosition)
}

/**
 * Wrapper function for creating a [Matcher] for text with an empty string
 */
fun isEmptyString(): Matcher<View> = withText("")

/**
 * Wrapper function for creating a [Matcher] for text with a non-empty string
 */
fun isNotEmptyString(): Matcher<View> = not(withText(""))

/**
 * VIEW ASSERTIONS
 */

/**
 * Wrapper function for creating a [NotPresentedViewAssertion]
 */
fun isNotPresented() = NotPresentedViewAssertion()

/**
 * TEXT SAVER
 */

/**
 * Most recently created [TextSaver], or null if none has been created
 */
private var lastSaver: TextSaver? = null

/**
 * Wrapper function for a [ViewAction] that saves the text of TextView.
 * Creates a new [TextSaver], and uses it to save text from TextView.
 * TextSaver is also stored in a local variable.
 *
 * @return [ViewAction]: the [TextSaver.SaveTextViewAction] associated with the new [TextSaver]
 */
fun saveText(): ViewAction {
    val saver = TextSaver()
    lastSaver = saver
    return saver.SaveTextViewAction()
}

/**
 * Wrapper function to get a matcher that checks if the text in a view matches the most recent saved text
 *
 * @return [Matcher]<[View]?>: the [TextSaver.PreviousTextViewMatcher] associated with the current [TextSaver],
 * or a matcher that always returns false if there is no saved matcher
 */
fun withSavedText(): Matcher<View?> {
    val falseMatcher = object : TypeSafeMatcher<View?>() {
        override fun describeTo(description: Description?) {}
        override fun matchesSafely(item: View?): Boolean = false
    }

    return lastSaver?.PreviousTextViewMatcher() ?: falseMatcher
}

/**
 * Delete current stored [TextSaver]
 */
fun clearSavedText() {
    lastSaver = null
}
