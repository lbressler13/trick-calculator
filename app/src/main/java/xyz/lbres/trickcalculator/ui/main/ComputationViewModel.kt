package xyz.lbres.trickcalculator.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.list.ext.copyWithoutLast
import xyz.lbres.trickcalculator.ui.history.HistoryItem
import xyz.lbres.trickcalculator.utils.isNumberChar

/**
 * ViewModel to track variables related to computation and computed values
 */
class ComputationViewModel : ViewModel() {
    /**
     * List of numbers and operators
     */
    private var _computeText: StringList = emptyList()
    val computeText: StringList
        get() = _computeText

    /**
     * Result of parsing compute text
     */
    private var _computedValue: ExactFraction? = null
    val computedValue: ExactFraction?
        get() = _computedValue

    /**
     * Error generated in computation
     */
    private var _error: String? = null
    val error: String?
        get() = _error

    /**
     * History item generated from most recent computation
     */
    private val _generatedHistoryItem = MutableLiveData<HistoryItem>().apply { value = null }
    val generatedHistoryItem: LiveData<HistoryItem> = _generatedHistoryItem

    /**
     * Backup values to use when generating history item
     */
    private var backupComputeText: StringList = emptyList()
    private var backupComputed: ExactFraction? = null

    fun setError(newValue: String) { _error = newValue }
    fun clearError() { _error = null }

    fun setComputedValue(newValue: ExactFraction) {
        backupComputeText = computeText
        backupComputed = computedValue
        _computedValue = newValue
    }

    /**
     * Store last history value based on most recent error or computation
     */
    fun generateHistoryItem() {
        val lastComputed = backupComputed
        val computed = computedValue
        var computation = backupComputeText

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
            error != null -> HistoryItem(computation, error!!, lastComputed)
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
    fun clearComputeText() { _computeText = emptyList() }
    private fun clearComputedValue() { _computedValue = null }

    private fun clearBackups() {
        backupComputeText = emptyList()
        backupComputed = null
    }

    /**
     * Append new value to end of list
     *
     * @param addition [String]: new character to add
     */
    fun appendComputeText(addition: String) {
        if (error != null) {
            clearError()
        }
        _computeText = computeText + addition
    }

    /**
     * Remove latest addition to compute text
     */
    fun backspaceComputeText() {
        if (computeText.isEmpty() && computedValue != null) {
            _computedValue = null
        } else if (computeText.isNotEmpty()) {
            _computeText = computeText.copyWithoutLast()
        }

        if (error != null) {
            clearError()
        }
    }

    /**
     * Save the current compute text, including the computed value
     */
    fun saveComputation() {
        val computedDecimal = computedValue?.toDecimalString()
        val computedString = ternaryIf(computedDecimal == null, emptyList(), listOf(computedDecimal!!))

        backupComputeText = computedString + computeText
    }

    /**
     * Set the compute text and last computed value based on a history item
     *
     * @param item [HistoryItem]: item containing compute text and previous computed value
     */
    fun useHistoryItemAsComputeText(item: HistoryItem) {
        resetComputeData()

        _computeText = item.computation
        if (item.previousResult != null) {
            _computedValue = item.previousResult
        }
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
            clearError()
        }
    }
}
