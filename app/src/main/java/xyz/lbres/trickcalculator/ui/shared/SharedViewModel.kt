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

    private val _applyDecimals = MutableLiveData<Boolean>().apply { value = true }
    val applyDecimals: LiveData<Boolean> = _applyDecimals
    fun setApplyDecimals(newValue: Boolean) { updateSetting(newValue, _applyDecimals) }

    private val _applyParens = MutableLiveData<Boolean>().apply { value = true }
    val applyParens: LiveData<Boolean> = _applyParens
    fun setApplyParens(newValue: Boolean) { updateSetting(newValue, _applyParens) }

    private val _clearOnError = MutableLiveData<Boolean>().apply { value = false }
    val clearOnError: LiveData<Boolean> = _clearOnError
    fun setClearOnError(newValue: Boolean) { updateSetting(newValue, _clearOnError) }

    private val _historyRandomness = MutableLiveData<Int>().apply { value = 1 }
    val historyRandomness: LiveData<Int> = _historyRandomness
    fun setHistoryRandomness(newValue: Int) { updateSetting(newValue, _historyRandomness) }

    private val _showSettingsButton = MutableLiveData<Boolean>().apply { value = false }
    val showSettingsButton: LiveData<Boolean> = _showSettingsButton
    fun setShowSettingsButton(newValue: Boolean) { updateSetting(newValue, _showSettingsButton) }

    private val _shuffleComputation = MutableLiveData<Boolean>().apply { value = false }
    val shuffleComputation: LiveData<Boolean> = _shuffleComputation
    fun setShuffleComputation(newValue: Boolean) { updateSetting(newValue, _shuffleComputation) }

    private val _shuffleNumbers = MutableLiveData<Boolean>().apply { value = false }
    val shuffleNumbers: LiveData<Boolean> = _shuffleNumbers
    fun setShuffleNumbers(newValue: Boolean) { updateSetting(newValue, _shuffleNumbers) }

    private val _shuffleOperators = MutableLiveData<Boolean>().apply { value = true }
    val shuffleOperators: LiveData<Boolean> = _shuffleOperators
    fun setShuffleOperators(newValue: Boolean) { updateSetting(newValue, _shuffleOperators) }

    /**
     * All settings
     */

    /**
     * Reset all settings other than displaying settings button on calculator fragment
     */
    fun resetSettings() {
        val defaults = Settings()
        setApplyDecimals(defaults.applyDecimals)
        setApplyParens(defaults.applyParens)
        setClearOnError(defaults.clearOnError)
        setHistoryRandomness(defaults.historyRandomness)
        setShuffleComputation(defaults.shuffleComputation)
        setShuffleNumbers(defaults.shuffleNumbers)
        setShuffleOperators(defaults.shuffleOperators)
    }

    /**
     * Select random settings, clear on error, and hide settings button on calculator fragment
     */
    fun randomizeSettings() {
        setApplyDecimals(random.nextBoolean())
        setApplyParens(random.nextBoolean())
        setHistoryRandomness((0..3).random())
        setShuffleComputation(random.nextBoolean())
        setShuffleNumbers(random.nextBoolean())
        setShuffleOperators(random.nextBoolean())

        setClearOnError(true)
        setShowSettingsButton(false)
    }

    /**
     * Set the calculator to use behave as a normal calculator. Does not affect settings button.
     */
    fun setStandardSettings() {
        setApplyDecimals(true)
        setApplyParens(true)
        setClearOnError(false)
        setHistoryRandomness(0)
        setShuffleComputation(false)
        setShuffleNumbers(false)
        setShuffleOperators(false)
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
