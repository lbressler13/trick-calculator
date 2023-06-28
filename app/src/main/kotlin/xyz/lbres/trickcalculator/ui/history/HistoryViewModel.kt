package xyz.lbres.trickcalculator.ui.history

import androidx.lifecycle.ViewModel
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.random.ext.nextBoolean
import xyz.lbres.trickcalculator.SharedValues.random
import xyz.lbres.trickcalculator.utils.History
import xyz.lbres.trickcalculator.utils.seededShuffled

/**
 * Information about computation history
 */
class HistoryViewModel : ViewModel() {
    /**
     * Randomness used in current shuffled history
     */
    private var _appliedRandomness: Int? = null
    var appliedRandomness: Int?
        get() = _appliedRandomness
        set(value) = updateRandomHistory(value ?: 0)

    /**
     * List of history items
     */
    private val _history: MutableList<HistoryItem> = mutableListOf()
    val history: History = _history

    /**
     * Values generated based on [history] and [appliedRandomness]
     */
    var randomizedHistory: History? = null
        private set

    /**
     * Update randomized history, based on current history and new randomness value
     *
     * @param randomness [Int]: new history randomness value from SettingsViewModel
     */
    private fun updateRandomHistory(randomness: Int) {
        _appliedRandomness = randomness
        randomizedHistory = getRandomHistory()
    }

    /**
     * Generate history, using the degree of randomness specified in the viewmodel
     */
    private fun getRandomHistory(): History {
        return when (appliedRandomness) {
            0 -> history // no randomness
            1 -> history.seededShuffled() // shuffled order
            2 -> shuffleHistoryValues() // shuffled values
            3 -> generateRandomHistory() ?: emptyList() // random generation
            else -> history
        }
    }

    /**
     * Shuffle history computations and results/errors.
     * Returns a list that contains all computations and results/errors, but not necessarily in their original pari.
     *
     * @return [History]: history where computations and values have been shuffled
     */
    private fun shuffleHistoryValues(): History {
        val computations: List<StringList> = history.map { it.computation }.seededShuffled()
        val values: List<Pair<ExactFraction?, String?>> = history.map { Pair(it.result, it.error) }.seededShuffled()

        val shuffledHistory = computations.mapIndexed { index, comp ->
            val valuePair = values[index]
            if (valuePair.first != null) {
                HistoryItem(comp, valuePair.first!!)
            } else {
                HistoryItem(comp, valuePair.second!!)
            }
        }

        return shuffledHistory
    }

    /**
     * Generate randomized history items based on the length of the input.
     * Returns null randomly or if history is empty, to indicate an "error" in retrieving history.
     *
     * @return [History]: possibly null history of generated computations, with same length as real history (if not null)
     */
    private fun generateRandomHistory(): History? {
        val probabilityError = ternaryIf(history.isEmpty(), 1f, 0.2f)

        if (random.nextBoolean(probabilityError)) {
            return null
        }

        return List(history.size) { generateRandomHistoryItem() }
    }

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
