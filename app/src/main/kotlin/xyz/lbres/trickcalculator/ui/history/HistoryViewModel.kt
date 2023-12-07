package xyz.lbres.trickcalculator.ui.history

import androidx.lifecycle.ViewModel
import xyz.lbres.trickcalculator.utils.History

/**
 * Information about computation history
 */
class HistoryViewModel : ViewModel() {
    /**
     * Randomness used in current shuffled history
     */
    private var _randomness: Int? = null
    var randomness: Int?
        get() = _randomness
        set(value) {
            _randomness = value
            randomizedHistory = generateRandomHistory(history, value)
        }

    /**
     * List of history items
     */
    private val _history: MutableList<HistoryItem> = mutableListOf()
    val history: History = _history

    /**
     * Values generated based on [history] and [randomness]
     */
    var randomizedHistory: History? = null
        private set

    /**
     * Add new item to history
     *
     * @param item [HistoryItem]
     */
    fun addToHistory(item: HistoryItem) {
        _history.add(item)
    }

    /**
     * Clear all values in history
     */
    fun clearHistory() {
        _history.clear()
        randomizedHistory = emptyList()
    }
}
