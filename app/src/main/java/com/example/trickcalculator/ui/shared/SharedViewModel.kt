package com.example.trickcalculator.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trickcalculator.ui.history.HistoryItem
import com.example.trickcalculator.ui.settings.Settings
import com.example.trickcalculator.utils.History
import java.util.*
import kotlin.random.Random

/**
 * ViewModel to track history and settings that are shared across fragments
 */
class SharedViewModel : ViewModel() {
    // settings
    private val mApplyDecimals = MutableLiveData<Boolean>().apply { value = true }
    val applyDecimals: LiveData<Boolean> = mApplyDecimals
    fun setApplyDecimals(newValue: Boolean) { mApplyDecimals.value = newValue }

    private val mApplyParens = MutableLiveData<Boolean>().apply { value = true }
    val applyParens: LiveData<Boolean> = mApplyParens
    fun setApplyParens(newValue: Boolean) { mApplyParens.value = newValue }

    private val mClearOnError = MutableLiveData<Boolean>().apply { value = false }
    val clearOnError: LiveData<Boolean> = mClearOnError
    fun setClearOnError(newValue: Boolean) { mClearOnError.value = newValue }

    private val mHistoryRandomness = MutableLiveData<Int>().apply { value = 1 }
    val historyRandomness: LiveData<Int> = mHistoryRandomness
    fun setHistoryRandomness(newValue: Int) { mHistoryRandomness.value = newValue }

    private val mShowSettingsButton = MutableLiveData<Boolean>().apply { value = false }
    val showSettingsButton: LiveData<Boolean> = mShowSettingsButton
    fun setShowSettingsButton(newValue: Boolean) { mShowSettingsButton.value = newValue }

    private val mShuffleComputation = MutableLiveData<Boolean>().apply { value = false }
    val shuffleComputation: LiveData<Boolean> = mShuffleComputation
    fun setShuffleComputation(newValue: Boolean) { mShuffleComputation.value = newValue }

    private val mShuffleNumbers = MutableLiveData<Boolean>().apply { value = false }
    val shuffleNumbers: LiveData<Boolean> = mShuffleNumbers
    fun setShuffleNumbers(newValue: Boolean) { mShuffleNumbers.value = newValue }

    private val mShuffleOperators = MutableLiveData<Boolean>().apply { value = true }
    val shuffleOperators: LiveData<Boolean> = mShuffleOperators
    fun setShuffleOperators(newValue: Boolean) { mShuffleOperators.value = newValue }

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
        setShowSettingsButton(false)
        setShuffleComputation(r.nextBoolean())
        setShuffleNumbers(r.nextBoolean())
        setShuffleOperators(r.nextBoolean())
    }

    // history
    private val mHistory = MutableLiveData<History>().apply { value = listOf() }
    val history: LiveData<History> = mHistory

    fun addToHistory(newItem: HistoryItem) {
        val currentHistory = history.value!!
        mHistory.value = currentHistory + newItem
    }

    fun clearHistory() {
        mHistory.value = listOf()
    }
}
