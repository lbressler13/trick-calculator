package xyz.lbres.trickcalculator.ui.attributions

import androidx.lifecycle.ViewModel
import xyz.lbres.kotlinutils.list.ext.copyWithReplacement

/**
 * ViewModel to track information about expanded/collapsed views in the Attributions fragment UI
 */
class AttributionsViewModel : ViewModel() {
    // whether or not the Flaticon message at top of screen is expanded
    var flaticonMessageExpanded = false
        private set
    fun setFlaticonMessageExpanded(newValue: Boolean) { flaticonMessageExpanded = newValue }

    // information about attributions
    var attributionsExpanded: List<Boolean> = emptyList()
        private set

    // if attributionsExpanded isn't already set, initialize to all closed
    fun initAttributionsExpanded(numAttributions: Int) {
        if (attributionsExpanded.size != numAttributions) {
            attributionsExpanded = List(numAttributions) { false }
        }
    }

    // set if a specific attribution is expanded
    fun setExpandedAt(newValue: Boolean, index: Int) {
        attributionsExpanded = attributionsExpanded.copyWithReplacement(index, newValue)
    }
}
