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

    /**
     * Individual settings
     *
     * Settings are represented as LiveData because fragments need to observe changes through the settings dialog
     */

    // private val _applyDecimals = MutableLiveData<Boolean>().apply { value = true }
    // val applyDecimals: LiveData<Boolean> = _applyDecimals
    // fun setApplyDecimals(newValue: Boolean) { updateSetting(newValue, _applyDecimals) }
    var applyDecimals: Boolean = true

//    private val _applyParens = MutableLiveData<Boolean>().apply { value = true }
//    val applyParens: LiveData<Boolean> = _applyParens
//    fun setApplyParens(newValue: Boolean) { updateSetting(newValue, _applyParens) }
    var applyParens: Boolean = true

    // private val _clearOnError = MutableLiveData<Boolean>().apply { value = false }
    // val clearOnError: LiveData<Boolean> = _clearOnError
    // fun setClearOnError(newValue: Boolean) { updateSetting(newValue, _clearOnError) }
    var clearOnError: Boolean = false

    // private val _historyRandomness = MutableLiveData<Int>().apply { value = 1 }
    // val historyRandomness: LiveData<Int> = _historyRandomness
    // fun setHistoryRandomness(newValue: Int) { updateSetting(newValue, _historyRandomness) }
    var historyRandomness: Int = 1

    // private val _showSettingsButton = MutableLiveData<Boolean>().apply { value = false }
    // val showSettingsButton: LiveData<Boolean> = _showSettingsButton
    // fun setShowSettingsButton(newValue: Boolean) { updateSetting(newValue, _showSettingsButton) }
    var showSettingsButton: Boolean = false

    // private val _shuffleComputation = MutableLiveData<Boolean>().apply { value = false }
    // val shuffleComputation: LiveData<Boolean> = _shuffleComputation
    // fun setShuffleComputation(newValue: Boolean) { updateSetting(newValue, _shuffleComputation) }
    var shuffleComputation: Boolean = false

    // private val _shuffleNumbers = MutableLiveData<Boolean>().apply { value = false }
    // val shuffleNumbers: LiveData<Boolean> = _shuffleNumbers
    // fun setShuffleNumbers(newValue: Boolean) { updateSetting(newValue, _shuffleNumbers) }
    var shuffleNumbers: Boolean = false

    // private val _shuffleOperators = MutableLiveData<Boolean>().apply { value = true }
    // val shuffleOperators: LiveData<Boolean> = _shuffleOperators
    // fun setShuffleOperators(newValue: Boolean) { updateSetting(newValue, _shuffleOperators) }
    var shuffleOperators: Boolean = true

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
        historyRandomness = defaults.historyRandomness
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
        historyRandomness = (0..3).random(random)
        shuffleComputation = random.nextBoolean()
        shuffleNumbers = random.nextBoolean()
        shuffleOperators = random.nextBoolean()

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
        historyRandomness = 0
        shuffleComputation = false
        shuffleNumbers = false
        shuffleOperators = false
    }

    /**
     * History
     */

    // LiveData because CalculatorFragment needs to observe when history is cleared
    private val _history = MutableLiveData<History>().apply { value = emptyList() }
    val history: LiveData<History> = _history

    fun addToHistory(newItem: HistoryItem) {
        val currentHistory = history.value!!
        _history.value = currentHistory + newItem
    }

    fun clearHistory() {
        _history.value = emptyList()
    }

    /**
     * Helpers
     */

    /**
     * Update the value of a setting if its value has changed.
     *
     * @param newValue [T]: new value for setting
     * @param mutable [MutableLiveData]<[T]>: mutable live data to be updated with the new value
     */
    private fun <T> updateSetting(newValue: T, mutable: MutableLiveData<T>) {
        if (newValue != mutable.value) {
            mutable.value = newValue
        }
    }
}
