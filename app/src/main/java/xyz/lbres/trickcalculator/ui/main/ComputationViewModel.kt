package xyz.lbres.trickcalculator.ui.main

import androidx.lifecycle.ViewModel
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
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
    var computeText: StringList = emptyList()
        private set

    /**
     * Result of parsing compute text
     */
    var computedValue: ExactFraction? = null
        private set

    /**
     * Error generated in computation
     */
    var error: String? = null
        private set

    /**
     * History item generated from most recent computation
     */
    var generatedHistoryItem: HistoryItem? = null
        private set

    /**
     * Backup values to use when generating history item
     */
    private var backupComputeText: StringList = emptyList()
    private var backupComputed: ExactFraction? = null

    fun setResult(newError: String?, newComputed: ExactFraction?, clearOnError: Boolean = false) {
        backupComputeText = computeText
        backupComputed = computedValue
        error = newError
        if (newComputed != null) {
            computedValue = newComputed
            computeText = emptyList()
        }

        generateHistoryItem()

        when {
            newComputed != null -> computeText = emptyList()
            newError != null && clearOnError -> resetComputeData(clearError = false)
        }
    }

    /**
     * Store last history value based on most recent error or computation
     */
    private fun generateHistoryItem() {
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

        if (lastComputed != null) {
            val decimalString = lastComputed.toDecimalString()
            computation = listOf(decimalString) + computation
        }

        generatedHistoryItem = when {
            error != null -> HistoryItem(computation, error!!, lastComputed)
            computed != null -> HistoryItem(computation, computed, lastComputed)
            else -> null
        }
    }

    /**
     * Remove the generated history item and historical values
     */
    fun clearStoredHistoryItem() {
        generatedHistoryItem = null
        clearBackups()
    }

    /**
     * Clear backup values
     */
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
            error = null
        }
        computeText = computeText + addition
    }

    /**
     * Remove latest addition to compute text
     */
    fun backspaceComputeText() {
        if (computeText.isEmpty() && computedValue != null) {
            computedValue = null
        } else if (computeText.isNotEmpty()) {
            computeText = computeText.copyWithoutLast()
        }

        if (error != null) {
            error = null
        }
    }

    /**
     * Set the compute text and last computed value based on a history item
     *
     * @param item [HistoryItem]: item containing compute text and previous computed value
     */
    fun useHistoryItemAsComputeText(item: HistoryItem) {
        resetComputeData()

        var computation = item.computation

        if (item.previousResult != null) {
            computedValue = item.previousResult
            computation = computation.subList(1, computation.size)
        }

        computeText = computation
    }

    /**
     * Reset data related to computation.
     * Settings are not reset.
     *
     * @param clearError [Boolean]: if error should be erased. True by default.
     * If false, error is not reset with other computation data.
     */
    fun resetComputeData(clearError: Boolean = true) {
        computeText = listOf()
        computedValue = null
        clearBackups()

        if (clearError) {
            error = null
        }
    }
}
