package com.example.trickcalculator.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.copyWithLastReplaced
import com.example.trickcalculator.ext.copyWithReplacement
import com.example.trickcalculator.ext.substringTo
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isInt
import com.example.trickcalculator.utils.isPartialDecimal

/**
 * ViewModel to track variables related to computation and settings
 */
class SharedViewModel : ViewModel() {
    // list of numbers and operators
    private val computeText = MutableLiveData<StringList>().apply { value = listOf() }

    // result of parsing computeText
    private val computedValue = MutableLiveData<ExactFraction?>().apply { value = null }

    private val error = MutableLiveData<String?>().apply { value = null }

    private val usesComputedValue = MutableLiveData<Boolean>().apply { value = false }

    // settings
    private val shuffleNumbers = MutableLiveData<Boolean>().apply { value = false }
    private val shuffleOperators = MutableLiveData<Boolean>().apply { value = true }
    private val applyParens = MutableLiveData<Boolean>().apply { value = true }
    private val clearOnError = MutableLiveData<Boolean>().apply { value = false }
    private val applyDecimals = MutableLiveData<Boolean>().apply { value = true }

    private val isDevMode = MutableLiveData<Boolean>().apply { value = false }

    // getters and setters for variables
    fun setError(newValue: String?) { error.value = newValue }
    fun getError(): LiveData<String?> = error

    fun setShuffleNumbers(newValue: Boolean) { shuffleNumbers.value = newValue }
    fun getShuffleNumbers(): LiveData<Boolean> = shuffleNumbers

    fun setShuffleOperators(newValue: Boolean) { shuffleOperators.value = newValue }
    fun getShuffleOperators(): LiveData<Boolean> = shuffleOperators

    fun setApplyParens(newValue: Boolean) { applyParens.value = newValue }
    fun getApplyParens(): LiveData<Boolean> = applyParens

    fun setClearOnError(newValue: Boolean) { clearOnError.value = newValue }
    fun getClearOnError(): LiveData<Boolean> = clearOnError

    fun setApplyDecimals(newValue: Boolean) { applyDecimals.value = newValue }
    fun getApplyDecimals(): LiveData<Boolean> = applyDecimals

    fun getComputeText(): LiveData<StringList> = computeText
    fun setComputedValue(newValue: ExactFraction) { computedValue.value = newValue }

    fun getIsDevMode(): LiveData<Boolean> = isDevMode
    fun setIsDevMode(newValue: Boolean) { isDevMode.value = newValue }

    fun getUsesComputedValues(): LiveData<Boolean> = usesComputedValue

    // clear current computed values
    private fun clearComputeText() { computeText.value = listOf() }
    private fun clearComputedValue() { computedValue.value = null }

    /**
     * Append new value to end of list, creating multi-digit number when possible
     *
     * @param addition [String]: new character to add
     */
    fun appendComputeText(addition: String) {
        val currentVal: StringList = computeText.value!!

        if (currentVal.size == 1 && computedValue.value != null) {
            computeText.value = currentVal + addition
        } else if (currentVal.isNotEmpty() && (isInt(addition) || addition == ".") &&
            // create multi-digit number
            isPartialDecimal(currentVal.last())) {
            val newAddition: String = currentVal.last() + addition
            computeText.value = currentVal.copyWithLastReplaced(newAddition)
        } else {
            computeText.value = currentVal + addition
        }
    }

    /**
     * Remove latest addition to compute text, possibly bt shortening a multi-digit number
     */
    fun backspaceComputeText() {
        val currentText: StringList = computeText.value!!

        if (currentText.size == 1 && computedValue.value != null) {
            usesComputedValue.value = false
            computeText.value = listOf()
            computedValue.value = null
        } else if (currentText.isNotEmpty()) {
            val lastValue = currentText.last()

            if (lastValue.length == 1) {
                computeText.value = currentText.subList(0, currentText.lastIndex)
            } else {
                // delete last digit from multi-digit number
                val newValue: String = lastValue.substringTo(lastValue.lastIndex)
                computeText.value = currentText.copyWithLastReplaced(newValue)
            }
        }
    }

    /**
     * Replace first value with EF string if it matched the computed value.
     * Used before running computation to retain exact value of last computed
     */
    fun finalizeComputeText() {
        val computedVal = computedValue.value
        val currentText = computeText.value

        val computeMatch = currentText?.isNotEmpty() == true && currentText[0] == computedVal?.toDecimalString()

        if (computedVal != null && computeMatch) {
            computeText.value = currentText?.copyWithReplacement(0, computedVal.toEFString())
        }
    }

    /**
     * Restore text to use initial value instead of EF string in case of error
     */
    fun restoreComputeText() {
        val computedVal = computedValue.value
        val currentText = computeText.value

        val firstIsEFString = currentText != null && currentText.isNotEmpty() && ExactFraction.isEFString(currentText[0])

        if (computedVal != null && firstIsEFString) {
            computeText.value = currentText?.copyWithReplacement(0, computedVal.toDecimalString())
        }
    }

    /**
     * Replace compute text list with the computed value
     */
    fun useComputedAsComputeText() {
        usesComputedValue.value = true
        val computed: ExactFraction = computedValue.value!!
        computeText.value = listOf(computed.toDecimalString())
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
            error.value = null
        }
    }
}