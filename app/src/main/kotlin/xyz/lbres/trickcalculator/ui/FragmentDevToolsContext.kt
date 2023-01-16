package xyz.lbres.trickcalculator.ui

import androidx.fragment.app.FragmentManager
import xyz.lbres.trickcalculator.utils.History

class FragmentDevToolsContext(
    val childFragmentManager: FragmentManager,
    val handleHistoryChange: (History) -> Unit
) {
    companion object {
        var currentContext: FragmentDevToolsContext? = null
    }
}
