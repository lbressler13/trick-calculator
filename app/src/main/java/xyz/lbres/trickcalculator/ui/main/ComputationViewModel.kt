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
    private val mComputeText = MutableLiveData<StringList>().apply { value = listOf() }
    val computeText: LiveData<StringList> = mComputeText

    /**
     * Result of parsing compute text
     */
    private val mComputedValue = MutableLiveData<ExactFraction>().apply { value = null }
    val computedValue: LiveData<ExactFraction> = mComputedValue

    /**
     * Error generated in computation
     */
    private val mError = MutableLiveData<String>().apply { value = null }
    val error: LiveData<String> = mError

    /**
     * History item generated from most recent computation
     */
    private val mGeneratedHistoryItem = MutableLiveData<HistoryItem>().apply { value = null }
    val generatedHistoryItem: LiveData<HistoryItem> = mGeneratedHistoryItem

    /**
     * Backup values to use when generating history item
     */
    private val mBackupComputeText = MutableLiveData<StringList>().apply { value = listOf() }
    private val mBackupComputed = MutableLiveData<ExactFraction>().apply { value = null }

    fun setError(newValue: String?) { mError.value = newValue }
    fun setComputedValue(newValue: ExactFraction) {
        mBackupComputed.value = mComputedValue.value
        mComputedValue.value = newValue
    }

    /**
     * Store last history value based on most recent error or computation
     */
    fun generateHistoryItem() {
        val error = error.value
        val lastComputed = mBackupComputed.value
        val computed = computedValue.value
        var computation = mBackupComputeText.value!!

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

        mGeneratedHistoryItem.value = when {
            error != null -> HistoryItem(computation, error, lastComputed)
            computed != null -> HistoryItem(computation, computed, lastComputed)
            else -> null
        }
    }

    /**
     * Remove the generated history item and historical values
     */
    fun clearStoredHistoryItem() {
        mGeneratedHistoryItem.value = null
        clearBackups()
    }

    /**
     * Clear computed values
     */
    fun clearComputeText() { mComputeText.value = listOf() }
    private fun clearComputedValue() { mComputedValue.value = null }
    private fun clearBackups() {
        mBackupComputeText.value = null
        mBackupComputed.value = null
    }

    /**
     * Append new value to end of list
     *
     * @param addition [String]: new character to add
     */
    fun appendComputeText(addition: String) {
        val currentVal: StringList = computeText.value!!
        mComputeText.value = currentVal + addition
    }

    /**
     * Remove latest addition to compute text
     */
    fun backspaceComputeText() {
        val currentText: StringList = computeText.value!!

        if (currentText.isEmpty() && computedValue.value != null) {
            mComputeText.value = listOf()
            mComputedValue.value = null
        } else if (currentText.isNotEmpty()) {
            mComputeText.value = currentText.subList(0, currentText.lastIndex)
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
        mBackupComputeText.value = computedString + currentComputeText
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

            mComputedValue.value = result
        }
        mComputeText.value = text
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
            mError.value = null
        }
    }
}
