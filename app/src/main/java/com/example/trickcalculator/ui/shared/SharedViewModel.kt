package com.example.trickcalculator.ui.shared

import android.util.Log
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
    private val mShuffleNumbers = MutableLiveData<Boolean>().apply { value = false }
    val shuffleNumbers: LiveData<Boolean> = mShuffleNumbers
    private val mShuffleOperators = MutableLiveData<Boolean>().apply { value = true }
    val shuffleOperators: LiveData<Boolean> = mShuffleOperators
    private val mApplyParens = MutableLiveData<Boolean>().apply { value = true }
    val applyParens: LiveData<Boolean> = mApplyParens
    private val mClearOnError = MutableLiveData<Boolean>().apply { value = false }
    val clearOnError: LiveData<Boolean> = mClearOnError
    private val mApplyDecimals = MutableLiveData<Boolean>().apply { value = true }
    val applyDecimals: LiveData<Boolean> = mApplyDecimals
    private val mShowSettingsButton = MutableLiveData<Boolean>().apply { value = false }
    val showSettingsButton: LiveData<Boolean> = mShowSettingsButton
    private val mHistoryRandomness = MutableLiveData<Int>().apply { value = 1 }
    val historyRandomness: LiveData<Int> = mHistoryRandomness
    private val mShuffleComputation = MutableLiveData<Boolean>().apply { value = false }
    val shuffleComputation: LiveData<Boolean> = mShuffleComputation

    fun setShuffleNumbers(newValue: Boolean) { mShuffleNumbers.value = newValue }
    fun setShuffleOperators(newValue: Boolean) { mShuffleOperators.value = newValue }
    fun setApplyParens(newValue: Boolean) { mApplyParens.value = newValue }
    fun setClearOnError(newValue: Boolean) { mClearOnError.value = newValue }
    fun setApplyDecimals(newValue: Boolean) { mApplyDecimals.value = newValue }
    fun setShowSettingsButton(newValue: Boolean) { mShowSettingsButton.value = newValue }
    fun setHistoryRandomness(newValue: Int) { mHistoryRandomness.value = newValue }
    fun setShuffleComputation(newValue: Boolean) { mShuffleComputation.value = newValue }

    // reset all settings other than settings button on main fragment
    fun resetSettings() {
        val defaults = Settings()
        setShuffleNumbers(defaults.shuffleNumbers)
        setShuffleOperators(defaults.shuffleOperators)
        setApplyParens(defaults.applyParens)
        setClearOnError(defaults.clearOnError)
        setApplyDecimals(defaults.applyDecimals)
        setHistoryRandomness(defaults.historyRandomness)
        setShuffleComputation(defaults.shuffleComputation)
    }

    // select random values for settings and hide settings button
    fun randomizeSettings() {
        val r = Random(Date().time)
        setShuffleNumbers(r.nextBoolean())
        setShuffleOperators(r.nextBoolean())
        setApplyParens(r.nextBoolean())
        setClearOnError(r.nextBoolean())
        setApplyDecimals(r.nextBoolean())
        setShuffleComputation(r.nextBoolean())
        setHistoryRandomness((0..3).random())
        setShowSettingsButton(false)
    }

    // dev mode
    private val mIsDevMode = MutableLiveData<Boolean>().apply { value = false }
    val isDevMode: LiveData<Boolean> = mIsDevMode
    fun setIsDevMode(newValue: Boolean) { mIsDevMode.value = newValue }

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