package com.example.trickcalculator.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trickcalculator.ext.copyWithLastReplaced
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isInt

class MainViewModel : ViewModel() {
    // list of numbers and operators
    private val computeText = MutableLiveData<StringList>().apply { value = listOf() }

    // result of parsing computeText
    private val computedValue = MutableLiveData<Int?>().apply { value = null }

    // settings
    private val shuffleNumbers = MutableLiveData<Boolean>().apply { value = false }
    private val shuffleOperators = MutableLiveData<Boolean>().apply { value = true }

    fun getComputeText(): LiveData<StringList> { return computeText }
    private fun clearComputeText() { computeText.value = listOf() }

    fun setComputedValue(newValue: Int) { computedValue.value = newValue }
    private fun clearComputedValue() { computedValue.value = null }

    fun setShuffleNumbers(newValue: Boolean) { shuffleNumbers.value = newValue }
    fun getShuffleNumbers(): LiveData<Boolean> { return shuffleNumbers }

    fun setShuffleOperators(newValue: Boolean) { shuffleOperators.value = newValue }
    fun getShuffleOperators(): LiveData<Boolean> { return shuffleOperators }

    // add new value to end of list
    fun appendComputeText(addition: String) {
        val currentVal: StringList = computeText.value!!

        // create multi-digit number
        if (currentVal.isNotEmpty() && isInt(addition) && isInt(currentVal.last())) {
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
                val newValue: String = lastValue.substring(0 until lastValue.lastIndex)
                computeText.value = currentText.copyWithLastReplaced(newValue)
            }
        }
    }

    // replace compute text list with the computed value
    fun useComputedAsComputeText() {
        val computed: Int = computedValue.value!!
        computeText.value = listOf(computed.toString())
    }

    fun resetComputeData() {
        clearComputeText()
        clearComputedValue()
    }
}