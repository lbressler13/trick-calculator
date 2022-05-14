package com.example.trickcalculator.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.copyWithLastReplaced
import com.example.trickcalculator.ext.copyWithReplacement
import com.example.trickcalculator.ext.substringTo
import com.example.trickcalculator.ui.history.HistoryItem
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isInt
import com.example.trickcalculator.utils.isPartialDecimal

/**
 * ViewModel to track variables related to computation and computed values
 */
class ComputationViewModel : ViewModel() {
    // list of numbers and operators
    private val mComputeText = MutableLiveData<StringList>().apply { value = listOf() }
    val computeText: LiveData<StringList> = mComputeText

    // result of parsing computeText
    private val mComputedValue = MutableLiveData<ExactFraction?>().apply { value = null }
    val computedValue: LiveData<ExactFraction?> = mComputedValue

    private val mError = MutableLiveData<String?>().apply { value = null }
    val error: LiveData<String?> = mError

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
        val computation = computeText.value!!

        mLastHistoryItem.value = when {
            error != null -> HistoryItem(computation, error)
            computed != null -> HistoryItem(computation, computed)
            else -> null
        }
    }

    fun removeLastHistory() {
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
        val currentVal: StringList = mComputeText.value!!

        if (currentVal.size == 1 && mComputedValue.value != null) {
            mComputeText.value = currentVal + addition
        } else if (currentVal.isNotEmpty() && (isInt(addition) || addition == ".") &&
            // create multi-digit number
            isPartialDecimal(currentVal.last())
        ) {
            val newAddition: String = currentVal.last() + addition
            mComputeText.value = currentVal.copyWithLastReplaced(newAddition)
        } else {
            mComputeText.value = currentVal + addition
        }
    }

    /**
     * Remove latest addition to compute text, possibly bt shortening a multi-digit number
     */
    fun backspaceComputeText() {
        val currentText: StringList = mComputeText.value!!

        if (currentText.size == 1 && mComputedValue.value != null) {
            mUsesComputedValue.value = false
            mComputeText.value = listOf()
            mComputedValue.value = null
        } else if (currentText.isNotEmpty()) {
            val lastValue = currentText.last()

            if (lastValue.length == 1) {
                mComputeText.value = currentText.subList(0, currentText.lastIndex)
            } else {
                // delete last digit from multi-digit number
                val newValue: String = lastValue.substringTo(lastValue.lastIndex)
                mComputeText.value = currentText.copyWithLastReplaced(newValue)
            }
        }
    }

    /**
     * Replace first value with EF string if it matched the computed value.
     * Used before running computation to retain exact value of last computed
     */
    fun finalizeComputeText() {
        val computedVal = mComputedValue.value
        val currentText = mComputeText.value

        val computeMatch = currentText?.isNotEmpty() == true && currentText[0] == computedVal?.toDecimalString()

        if (computedVal != null && computeMatch) {
            mComputeText.value = currentText?.copyWithReplacement(0, computedVal.toEFString())
        }
    }

    /**
     * Restore text to use initial value instead of EF string in case of error
     */
    fun restoreComputeText() {
        val computedVal = mComputedValue.value
        val currentText = mComputeText.value

        val firstIsEFString = currentText != null && currentText.isNotEmpty() && ExactFraction.isEFString(currentText[0])

        if (computedVal != null && firstIsEFString) {
            mComputeText.value = currentText?.copyWithReplacement(0, computedVal.toDecimalString())
        }
    }

    /**
     * Replace compute text list with the computed value
     */
    fun useComputedAsComputeText() {
        mUsesComputedValue.value = true
        val computed: ExactFraction = mComputedValue.value!!
        mComputeText.value = listOf(computed.toDecimalString())
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