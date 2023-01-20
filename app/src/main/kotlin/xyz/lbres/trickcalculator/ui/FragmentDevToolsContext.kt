package xyz.lbres.trickcalculator.ui

import androidx.fragment.app.FragmentManager

/**
 * Data about a fragment to be used by the dev tools dialog
 *
 * @param childFragmentManager [FragmentManager]
 * @param handleHistoryCleared () -> Unit: function to call when history is cleared
 */
data class FragmentDevToolsContext(
    val childFragmentManager: FragmentManager,
    val handleHistoryCleared: () -> Unit
) {
    companion object {
        /**
         * Context for current fragment
         */
        var currentContext: FragmentDevToolsContext? = null
    }
}
