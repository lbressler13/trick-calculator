package com.example.trickcalculator.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trickcalculator.ext.copyWithReplacement
import com.example.trickcalculator.ui.history.HistoryItem
import com.example.trickcalculator.utils.*
import exactfraction.ExactFraction

/**
 * ViewModel to track variables related to computation and computed values
 */
class ComputationViewModel : ViewModel() {
    // list of numbers and operators
    private val mComputeText = MutableLiveData<StringList>().apply { value = listOf() }
    val computeText: LiveData<StringList> = mComputeText
    private val mBackupComputeText = MutableLiveData<StringList>().apply { value = listOf() }

    // result of parsing computeText
    private val mComputedValue = MutableLiveData<ExactFraction>().apply { value = null }
    val computedValue: LiveData<ExactFraction> = mComputedValue

    private val mError = MutableLiveData<String>().apply { value = null }
    val error: LiveData<String> = mError

    private val mUsesComputedValue = MutableLiveData<Boolean>().apply { value = false }
    val usesComputedValue: LiveData<Boolean> = mUsesComputedValue

    private val mLastHistoryItem = MutableLiveData<HistoryItem>().apply { value = null }
    val lastHistoryItem: LiveData<HistoryItem> = mLastHistoryItem

    fun setError(newValue: String?) { mError.value = newValue }
    fun setComputedValue(newValue: ExactFraction) { mComputedValue.value = newValue }

    /**
     * Store last history value based on most recent error or computation
     */
    fun setLastHistoryItem() {
        val error = error.value
        val computed = computedValue.value
        var computation = mBackupComputeText.value!!

        if (computation[0].startsWith('[')) {
            val first = computation[0]
            val updatedFirst = first.substring(1 until first.lastIndex)
            computation = computation.copyWithReplacement(0, updatedFirst)
        }

        mLastHistoryItem.value = when {
            error != null -> HistoryItem(computation, error)
            computed != null -> HistoryItem(computation, computed)
            else -> null
        }
    }

    fun clearStoredHistoryItem() {
        mLastHistoryItem.value = null
    }

    /**
     * Clear current computed values
     */
    private fun clearComputeText() { mComputeText.value = listOf() }
    private fun clearComputedValue() {
        mComputedValue.value = null
        mUsesComputedValue.value = false
    }

    /**
     * Append new value to end of list, creating multi-digit number when possible
     *
     * @param addition [String]: new character to add
     */
    fun appendComputeText(addition: String) {
        val currentVal: StringList = computeText.value!!
        mComputeText.value = currentVal + addition
    }

    /**
     * Remove latest addition to compute text, possibly bt shortening a multi-digit number
     */
    fun backspaceComputeText() {
        val currentText: StringList = computeText.value!!

        if (currentText.size == 1 && computedValue.value != null) {
            mUsesComputedValue.value = false
            mComputeText.value = listOf()
            mComputedValue.value = null
        } else if (currentText.isNotEmpty()) {
            mComputeText.value = currentText.subList(0, currentText.lastIndex)
        }
    }

    /**
     * Replace first value with EF string if it matched the computed value.
     * Used before running computation to retain exact value of last computed
     */
    fun finalizeComputeText() {
        val currentText = computeText.value
        mBackupComputeText.value = currentText

        if (usesComputedValue.value == true && currentText!!.size == 1) {
            mComputeText.value = listOf()
        } else if (usesComputedValue.value == true && currentText!!.size > 1) {
            var updatedText = currentText.subList(1, currentText.size)
            val nextValue = updatedText[0]

            if (nextValue == "(" || isNumberChar(nextValue)) {
                updatedText = listOf("x") + updatedText
            }
            mComputeText.value = updatedText
        }
    }

    /**
     * Restore text to use initial value instead of EF string in case of error
     */
    fun restoreComputeText() {
        mComputeText.value = mBackupComputeText.value
    }

    /**
     * Replace compute text list with the computed value
     */
    fun useComputedAsComputeText() {
        mUsesComputedValue.value = true
        mComputeText.value = listOf(getBracketedValue()!!)
    }

    /**
     * Get currently computed item, surrounded by square brackets
     */
    private fun getBracketedValue(): String? {
        val text = computedValue.value?.toDecimalString()
        if (text == null) {
            return null
        }

        return "[$text]"
    }

    /**
     * Reset data related to computation.
     * Settings are not reset.
     *
     * @param clearError [Boolean]: if error should be erased. True by default.
     * If false, error is not reset with other computation data.
     */
    fun resetComputeData(clearError: Boolean = true) {
        clearComputeText()
        clearComputedValue()

        if (clearError) {
            mError.value = null
        }
    }
}
