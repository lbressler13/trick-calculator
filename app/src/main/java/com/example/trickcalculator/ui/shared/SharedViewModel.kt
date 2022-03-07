package com.example.trickcalculator.ui.shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trickcalculator.bigfraction.BigFraction
import com.example.trickcalculator.ext.copyWithLastReplaced
import com.example.trickcalculator.ext.copyWithReplacement
import com.example.trickcalculator.ext.substringTo
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isInt
import com.example.trickcalculator.utils.isPartialDecimal

class SharedViewModel : ViewModel() {
    // list of numbers and operators
    private val computeText = MutableLiveData<StringList>().apply { value = listOf() }

    // result of parsing computeText
    private val computedValue = MutableLiveData<BigFraction?>().apply { value = null }

    private val error = MutableLiveData<String?>().apply { value = null }

    // settings
    private val shuffleNumbers = MutableLiveData<Boolean>().apply { value = false }
    private val shuffleOperators = MutableLiveData<Boolean>().apply { value = true }
    private val applyParens = MutableLiveData<Boolean>().apply { value = true }
    private val clearOnError = MutableLiveData<Boolean>().apply { value = true }
    private val applyDecimals = MutableLiveData<Boolean>().apply { value = true }

    fun getComputeText(): LiveData<StringList> = computeText
    private fun clearComputeText() {
        computeText.value = listOf()
    }

    fun setComputedValue(newValue: BigFraction) {
        computedValue.value = newValue
    }

    private fun clearComputedValue() {
        computedValue.value = null
    }

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

    // add new value to end of list
    fun appendComputeText(addition: String) {
        val currentVal: StringList = computeText.value!!

        // create multi-digit number
        if (currentVal.isNotEmpty() && (isInt(addition) || addition == ".") &&
            isPartialDecimal(currentVal.last())) {
            val newAddition: String = currentVal.last() + addition
            computeText.value = currentVal.copyWithLastReplaced(newAddition)
        } else {
            computeText.value = currentVal + addition
        }
    }

    // delete last value from list
    fun backspaceComputeText() {
        val currentText: StringList = computeText.value!!

        if (currentText.isNotEmpty()) {
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

    fun finalizeComputeText() {
        val computedVal = computedValue.value
        val currentText = computeText.value

        val computeMatch = currentText?.isNotEmpty() == true && currentText[0] == computedVal?.toString()

        if (computedVal != null && computeMatch) {
            computeText.value = currentText?.copyWithReplacement(0, computedVal.toBFString())
        }
    }

    fun restoreComputeText() {
        val computedVal = computedValue.value
        val currentText = computeText.value
        if (computedVal != null) {
            computeText.value = currentText?.copyWithReplacement(0, computedVal.toString())
        }
    }

    // replace compute text list with the computed value
    fun useComputedAsComputeText() {
        val computed: BigFraction = computedValue.value!!

        Log.e("bf", computed.toBFString())
        Log.e("dec", computed.toDecimalString())

        computeText.value = listOf(computed.toString())
    }

    fun resetComputeData(clearError: Boolean = true) {
        clearComputeText()
        clearComputedValue()

        if (clearError) {
            error.value = null
        }
    }
}