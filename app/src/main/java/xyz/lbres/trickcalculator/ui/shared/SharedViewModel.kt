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
    /**
     * Individual settings
     */

    private val _applyDecimals = MutableLiveData<Boolean>().apply { value = true }
    val applyDecimals: LiveData<Boolean> = _applyDecimals
    fun setApplyDecimals(newValue: Boolean) { _applyDecimals.value = newValue }

    private val _applyParens = MutableLiveData<Boolean>().apply { value = true }
    val applyParens: LiveData<Boolean> = _applyParens
    fun setApplyParens(newValue: Boolean) { _applyParens.value = newValue }

    private val _clearOnError = MutableLiveData<Boolean>().apply { value = false }
    val clearOnError: LiveData<Boolean> = _clearOnError
    fun setClearOnError(newValue: Boolean) { _clearOnError.value = newValue }

    private val _historyRandomness = MutableLiveData<Int>().apply { value = 1 }
    val historyRandomness: LiveData<Int> = _historyRandomness
    fun setHistoryRandomness(newValue: Int) { _historyRandomness.value = newValue }

    private val _showSettingsButton = MutableLiveData<Boolean>().apply { value = false }
    val showSettingsButton: LiveData<Boolean> = _showSettingsButton
    fun setShowSettingsButton(newValue: Boolean) { _showSettingsButton.value = newValue }

    private val _shuffleComputation = MutableLiveData<Boolean>().apply { value = false }
    val shuffleComputation: LiveData<Boolean> = _shuffleComputation
    fun setShuffleComputation(newValue: Boolean) { _shuffleComputation.value = newValue }

    private val _shuffleNumbers = MutableLiveData<Boolean>().apply { value = false }
    val shuffleNumbers: LiveData<Boolean> = _shuffleNumbers
    fun setShuffleNumbers(newValue: Boolean) { _shuffleNumbers.value = newValue }

    private val _shuffleOperators = MutableLiveData<Boolean>().apply { value = true }
    val shuffleOperators: LiveData<Boolean> = _shuffleOperators
    fun setShuffleOperators(newValue: Boolean) { _shuffleOperators.value = newValue }

    /**
     * All settings
     */

    // reset all settings other than settings button on main fragment
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

    // select random values for settings and hide settings button
    fun randomizeSettings() {
        val r = Random(Date().time)
        setApplyDecimals(r.nextBoolean())
        setApplyParens(r.nextBoolean())
        setClearOnError(r.nextBoolean())
        setHistoryRandomness((0..3).random())
        setShuffleComputation(r.nextBoolean())
        setShuffleNumbers(r.nextBoolean())
        setShuffleOperators(r.nextBoolean())

        setShowSettingsButton(false)
    }

    /**
     * History
     */

    private val _history = MutableLiveData<History>().apply { value = listOf() }
    val history: LiveData<History> = _history

    fun addToHistory(newItem: HistoryItem) {
        val currentHistory = history.value!!
        _history.value = currentHistory + newItem
    }

    fun clearHistory() {
        _history.value = listOf()
    }
}
