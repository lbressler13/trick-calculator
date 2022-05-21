package com.example.trickcalculator.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trickcalculator.ext.copyWithLastReplaced
import com.example.trickcalculator.ext.copyWithReplacement
import com.example.trickcalculator.ext.substringTo
import com.example.trickcalculator.ui.history.HistoryItem
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isInt
import com.example.trickcalculator.utils.isNumber
import com.example.trickcalculator.utils.isPartialDecimal
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
        var computation = computeText.value!!

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
        mBackupComputeText.value = currentText

        val computeMatch = currentText?.isNotEmpty() == true && currentText[0] == getBracketedValue()

        if (computedVal != null && computeMatch && currentText != null) {
            var updatedText = currentText.copyWithReplacement(0, computedVal.toEFString())
            if (currentText.size > 1 && isNumber(currentText[1])) {
                updatedText = listOf(updatedText[0], "x") + updatedText.subList(1, updatedText.size)
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
