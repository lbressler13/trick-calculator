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
     */

    // LiveData because fragments need to observe when settings are changed via settings dialog
    private val _applyDecimals = MutableLiveData<Boolean>().apply { value = true }
    val applyDecimals: LiveData<Boolean> = _applyDecimals
    fun setApplyDecimals(newValue: Boolean) {
        if (newValue != applyDecimals.value) {
            _applyDecimals.value = newValue
        }
    }

    private val _applyParens = MutableLiveData<Boolean>().apply { value = true }
    val applyParens: LiveData<Boolean> = _applyParens
    fun setApplyParens(newValue: Boolean) {
        if (newValue != applyParens.value) {
            _applyParens.value = newValue
        }
    }

    private val _clearOnError = MutableLiveData<Boolean>().apply { value = false }
    val clearOnError: LiveData<Boolean> = _clearOnError
    fun setClearOnError(newValue: Boolean) {
        if (newValue != clearOnError.value) {
            _clearOnError.value = newValue
        }
    }

    private val _historyRandomness = MutableLiveData<Int>().apply { value = 1 }
    val historyRandomness: LiveData<Int> = _historyRandomness
    fun setHistoryRandomness(newValue: Int) {
        if (newValue != historyRandomness.value) {
            _historyRandomness.value = newValue
        }
    }

    private val _showSettingsButton = MutableLiveData<Boolean>().apply { value = false }
    val showSettingsButton: LiveData<Boolean> = _showSettingsButton
    fun setShowSettingsButton(newValue: Boolean) {
        if (newValue != showSettingsButton.value) {
            _showSettingsButton.value = newValue
        }
    }

    private val _shuffleComputation = MutableLiveData<Boolean>().apply { value = false }
    val shuffleComputation: LiveData<Boolean> = _shuffleComputation
    fun setShuffleComputation(newValue: Boolean) {
        if (newValue != shuffleComputation.value) {
            _shuffleComputation.value = newValue
        }
    }

    private val _shuffleNumbers = MutableLiveData<Boolean>().apply { value = false }
    val shuffleNumbers: LiveData<Boolean> = _shuffleNumbers
    fun setShuffleNumbers(newValue: Boolean) {
        if (newValue != shuffleNumbers.value) {
            _shuffleNumbers.value = newValue
        }
    }

    private val _shuffleOperators = MutableLiveData<Boolean>().apply { value = true }
    val shuffleOperators: LiveData<Boolean> = _shuffleOperators
    fun setShuffleOperators(newValue: Boolean) {
        if (newValue != shuffleOperators.value) {
            _shuffleOperators.value = newValue
        }
    }

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
     * Select random settings and hide settings button on calculator fragment
     */
    fun randomizeSettings() {
        setApplyDecimals(random.nextBoolean())
        setApplyParens(random.nextBoolean())
        setClearOnError(random.nextBoolean())
        setHistoryRandomness((0..3).random())
        setShuffleComputation(random.nextBoolean())
        setShuffleNumbers(random.nextBoolean())
        setShuffleOperators(random.nextBoolean())

        setShowSettingsButton(false)
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
}
