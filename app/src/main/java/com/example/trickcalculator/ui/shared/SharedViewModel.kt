package com.example.trickcalculator.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trickcalculator.ui.history.HistoryItem

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
    private val mHistoryRandomness = MutableLiveData<Int>().apply { value = 0 }
    val historyRandomness: LiveData<Int> = mHistoryRandomness

    fun setShuffleNumbers(newValue: Boolean) { mShuffleNumbers.value = newValue }
    fun setShuffleOperators(newValue: Boolean) { mShuffleOperators.value = newValue }
    fun setApplyParens(newValue: Boolean) { mApplyParens.value = newValue }
    fun setClearOnError(newValue: Boolean) { mClearOnError.value = newValue }
    fun setApplyDecimals(newValue: Boolean) { mApplyDecimals.value = newValue }
    fun setShowSettingsButton(newValue: Boolean) { mShowSettingsButton.value = newValue }
    fun setHistoryRandomness(newValue: Int) { mHistoryRandomness.value = newValue }

    // dev mode
    private val mIsDevMode = MutableLiveData<Boolean>().apply { value = false }
    val isDevMode: LiveData<Boolean> = mIsDevMode
    fun setIsDevMode(newValue: Boolean) { mIsDevMode.value = newValue }

    // history
    private val mHistory = MutableLiveData<List<HistoryItem>>().apply { value = listOf() }
    val history: LiveData<List<HistoryItem>> = mHistory

    fun addToHistory(newItem: HistoryItem) {
        val currentHistory = history.value!!
        mHistory.value = currentHistory + newItem
    }
}