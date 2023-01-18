package xyz.lbres.trickcalculator.ui.history

import androidx.lifecycle.ViewModel
import xyz.lbres.trickcalculator.utils.History

/**
 * Information about values that are currently displayed on history screen
 */
class HistoryViewModel : ViewModel() {
    /**
     * Unmodified displayed on screen
     */
    var history: History? = null
        private set

    /**
     * Randomness applied to items on screen
     */
    var randomness: Int? = null

    /**
     * Values generated based on [history] and [randomness]
     */
    var randomizedHistory: History? = null

    // TODO move code for creating history to VM

    /**
     * Set history by copying values into new list
     *
     * @param newValue [History]?
     */
    fun setHistory(newValue: History?) {
        history = newValue?.toList()
    }

    /**
     * Clear all values in ViewModel
     */
    fun clearValues() {
        randomness = null
        history = null
        randomizedHistory = null
    }
}
