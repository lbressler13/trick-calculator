package xyz.lbres.trickcalculator.ui.attributions

import androidx.lifecycle.ViewModel
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions

/**
 * ViewModel to track information about expanded/collapsed views in the Attributions fragment UI
 */
class AttributionsViewModel : ViewModel() {
    /**
     * If Flaticon message at top of screen is expanded
     */
    var flaticonMessageExpanded = false

    /**
     * If attributions are expanded
     */
    val attributionsExpanded: BooleanArray = BooleanArray(authorAttributions.size) { false }
}
