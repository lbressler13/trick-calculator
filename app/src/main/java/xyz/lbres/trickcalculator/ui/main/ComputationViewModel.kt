package xyz.lbres.trickcalculator.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.ui.history.HistoryItem
import xyz.lbres.trickcalculator.utils.isNumberChar

/**
 * ViewModel to track variables related to computation and computed values
 */
class ComputationViewModel : ViewModel() {
    /**
     * List of numbers and operators
     */
    private val _computeText = MutableLiveData<StringList>().apply { value = listOf() }
    val computeText: LiveData<StringList> = _computeText

    /**
     * Result of parsing compute text
     */
    private val _computedValue = MutableLiveData<ExactFraction>().apply { value = null }
    val computedValue: LiveData<ExactFraction> = _computedValue

    /**
     * Error generated in computation
     */
    private val _error = MutableLiveData<String>().apply { value = null }
    val error: LiveData<String> = _error

    /**
     * History item generated from most recent computation
     */
    private val _generatedHistoryItem = MutableLiveData<HistoryItem>().apply { value = null }
    val generatedHistoryItem: LiveData<HistoryItem> = _generatedHistoryItem

    /**
     * Backup values to use when generating history item
     */
    private val backupComputeText = MutableLiveData<StringList>().apply { value = listOf() }
    private val backupComputed = MutableLiveData<ExactFraction>().apply { value = null }

    fun setError(newValue: String?) { _error.value = newValue }
    fun setComputedValue(newValue: ExactFraction) {
        backupComputed.value = _computedValue.value
        _computedValue.value = newValue
    }

    /**
     * Store last history value based on most recent error or computation
     */
    fun generateHistoryItem() {
        val error = error.value
        val lastComputed = backupComputed.value
        val computed = computedValue.value
        var computation = backupComputeText.value!!

        // if computed val was used and next item is a number, pad with times symbol
        if (
            computation.size > 1 &&
            lastComputed != null &&
            computation[0] == lastComputed.toDecimalString() &&
            isNumberChar(computation[1])
        ) {
            val start = listOf(computation[0], "x")
            computation = start + computation.subList(1, computation.size)
        }

        _generatedHistoryItem.value = when {
            error != null -> HistoryItem(computation, error, lastComputed)
            computed != null -> HistoryItem(computation, computed, lastComputed)
            else -> null
        }
    }

    /**
     * Remove the generated history item and historical values
     */
    fun clearStoredHistoryItem() {
        _generatedHistoryItem.value = null
        clearBackups()
    }

    /**
     * Clear computed values
     */
    fun clearComputeText() { _computeText.value = listOf() }
    private fun clearComputedValue() { _computedValue.value = null }
    private fun clearBackups() {
        backupComputeText.value = null
        backupComputed.value = null
    }

    /**
     * Append new value to end of list
     *
     * @param addition [String]: new character to add
     */
    fun appendComputeText(addition: String) {
        val currentVal: StringList = computeText.value!!
        _computeText.value = currentVal + addition
    }

    /**
     * Remove latest addition to compute text
     */
    fun backspaceComputeText() {
        val currentText: StringList = computeText.value!!

        if (currentText.isEmpty() && computedValue.value != null) {
            _computeText.value = listOf()
            _computedValue.value = null
        } else if (currentText.isNotEmpty()) {
            _computeText.value = currentText.subList(0, currentText.lastIndex)
        }
    }

    /**
     * Save the current compute text, including the computed value
     */
    fun saveComputation() {
        val computedDecimal = computedValue.value?.toDecimalString()
        val computedString = if (computedDecimal == null) {
            listOf()
        } else {
            listOf(computedDecimal)
        }
        val currentComputeText = computeText.value!!
        backupComputeText.value = computedString + currentComputeText
    }

    /**
     * Set the compute text and last computed value based on a history item
     *
     * @param item [HistoryItem]: item containing compute text and previous computed value
     */
    fun useHistoryItemAsComputeText(item: HistoryItem) {
        resetComputeData()
        var text = item.computation
        val result = item.previousResult

        if (result != null) {
            // first value of text is the computed value
            if (text.isNotEmpty()) {
                text = text.subList(1, text.size)
            }

            _computedValue.value = result
        }
        _computeText.value = text
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
        clearBackups()

        if (clearError) {
            _error.value = null
        }
    }
}
