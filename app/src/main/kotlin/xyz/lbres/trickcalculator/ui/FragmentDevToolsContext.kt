package xyz.lbres.trickcalculator.ui

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import xyz.lbres.trickcalculator.utils.History

data class FragmentDevToolsContext(
    val childFragmentManager: FragmentManager,
    val lifecycleOwner: LifecycleOwner,
    val handleHistoryChange: (History) -> Unit,
    val handleSettingsChange: () -> Unit
)
