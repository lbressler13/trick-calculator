package xyz.lbres.trickcalculator.ui

import androidx.fragment.app.FragmentManager
import xyz.lbres.trickcalculator.utils.History

data class FragmentDevToolsContext(
    val childFragmentManager: FragmentManager,
    val handleHistoryChange: (History) -> Unit,
    val handleSettingsChange: () -> Unit
)
