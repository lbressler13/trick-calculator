package xyz.lbres.trickcalculator.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.lbres.trickcalculator.ui.history.HistoryItem
import xyz.lbres.trickcalculator.ui.settings.Settings
import xyz.lbres.trickcalculator.utils.History
import java.util.Date
import kotlin.random.Random

/**
 * ViewModel to track history and settings that are shared across fragments
 */
class SharedViewModel : ViewModel() {
    private val random = Random(Date().time)

    private val _uiValuesChanged = MutableLiveData<Boolean>().apply { value = false }
    val uiValuesChanged: LiveData<Boolean> = _uiValuesChanged

    /**
     * Individual settings
     */
    var applyDecimals: Boolean = true
    var applyParens: Boolean = true
    var clearOnError: Boolean = false
    var historyRandomness: Int = 1
        private set
    var showSettingsButton: Boolean = false
    var shuffleComputation: Boolean = false
    var shuffleNumbers: Boolean = false
    var shuffleOperators: Boolean = true

    fun setHistoryRandomness(newValue: Int) {
        if (newValue != historyRandomness) {
            historyRandomness = newValue
            _uiValuesChanged.value = true
        }
    }

    fun historyRandomnessApplied() {
        _uiValuesChanged.value = false
    }

    /**
     * All settings
     */

    /**
     * Reset all settings other than displaying settings button on calculator fragment
     */
    fun resetSettings() {
        val defaults = Settings()
        applyDecimals = defaults.applyDecimals
        applyParens = defaults.applyParens
        clearOnError = defaults.clearOnError
        setHistoryRandomness(defaults.historyRandomness)
        shuffleComputation = defaults.shuffleComputation
        shuffleNumbers = defaults.shuffleNumbers
        shuffleOperators = defaults.shuffleOperators
    }

    /**
     * Select random settings, clear on error, and hide settings button on calculator fragment
     */
    fun randomizeSettings() {
        applyDecimals = random.nextBoolean()
        applyParens = random.nextBoolean()
        shuffleComputation = random.nextBoolean()
        shuffleNumbers = random.nextBoolean()
        shuffleOperators = random.nextBoolean()

        val newHistoryRandomness = (0..3).random(random)
        setHistoryRandomness(newHistoryRandomness)

        clearOnError = true
        showSettingsButton = false
    }

    /**
     * Set the calculator to use behave as a normal calculator. Does not affect settings button.
     */
    fun setStandardSettings() {
        applyDecimals = true
        applyParens = true
        clearOnError = false
        setHistoryRandomness(0)
        shuffleComputation = false
        shuffleNumbers = false
        shuffleOperators = false
    }

    /**
     * History
     */

    private val _history: MutableList<HistoryItem> = mutableListOf()
    val history: History = _history

    fun addToHistory(newItem: HistoryItem) {
        _history.add(newItem)
    }

    fun clearHistory() {
        _history.clear()
    }
}
