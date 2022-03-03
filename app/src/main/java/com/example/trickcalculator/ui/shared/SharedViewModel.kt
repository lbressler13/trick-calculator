package com.example.trickcalculator.ui.shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trickcalculator.ext.copyWithLastReplaced
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isInt
import com.example.trickcalculator.utils.isPartialDecimal

class SharedViewModel : ViewModel() {
    // list of numbers and operators
    private val computeText = MutableLiveData<StringList>().apply { value = listOf() }

    // result of parsing computeText
    private val computedValue = MutableLiveData<Float?>().apply { value = null }

    private val error = MutableLiveData<String?>().apply { value = null }

    // settings
    private val shuffleNumbers = MutableLiveData<Boolean>().apply { value = false }
    private val shuffleOperators = MutableLiveData<Boolean>().apply { value = true }
    private val applyParens = MutableLiveData<Boolean>().apply { value = true }
    private val clearOnError = MutableLiveData<Boolean>().apply { value = true }

    fun getComputeText(): LiveData<StringList> = computeText
    private fun clearComputeText() {
        computeText.value = listOf()
    }

    fun setComputedValue(newValue: Float) {
        computedValue.value = newValue
    }

    private fun clearComputedValue() {
        computedValue.value = null
    }

    fun setError(newValue: String?) {
        error.value = newValue
    }

    fun getError(): LiveData<String?> = error

    fun setShuffleNumbers(newValue: Boolean) {
        shuffleNumbers.value = newValue
    }

    fun getShuffleNumbers(): LiveData<Boolean> = shuffleNumbers

    fun setShuffleOperators(newValue: Boolean) {
        shuffleOperators.value = newValue
    }

    fun getShuffleOperators(): LiveData<Boolean> = shuffleOperators

    fun setApplyParens(newValue: Boolean) {
        applyParens.value = newValue
    }

    fun getApplyParens(): LiveData<Boolean> = applyParens

    fun setClearOnError(newValue: Boolean) {
        clearOnError.value = newValue
    }

    fun getClearOnError(): LiveData<Boolean> = clearOnError

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
        Log.e(null, computeText.value.toString())
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
                val newValue: String = lastValue.substring(0 until lastValue.lastIndex)
                computeText.value = currentText.copyWithLastReplaced(newValue)
            }
        }
    }

    // replace compute text list with the computed value
    fun useComputedAsComputeText() {
        val computed: Float = computedValue.value!!
        val intVal: Int = computed.toInt()

        computeText.value = if (computed == intVal.toFloat()) {
            listOf(intVal.toString())
        } else {
            listOf(computed.toString())
        }

        // computeText.value = listOf(computed.toString())
    }

    fun resetComputeData(clearError: Boolean = true) {
        clearComputeText()
        clearComputedValue()

        if (clearError) {
            error.value = null
        }
    }
}