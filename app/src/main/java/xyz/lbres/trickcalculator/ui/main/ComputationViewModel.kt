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

    /**
     * Set error or computed result, update compute text, and create history item based on result.
     *
     * @param newError [String]?: error result, null if no error was thrown
     * @param newComputed [ExactFraction]?: computed result, null if result was not computed successfully
     * @param clearOnError [Boolean]: if compute data should be cleared in event of an error. Defaults to false
     */
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
        var lastComputeText = backupComputeText

        // add computed text to start of computation
        if (lastComputed != null) {
            val decimalString = lastComputed.toDecimalString()
            lastComputeText = listOf(decimalString) + lastComputeText
        }

        // if computed val was used and next item is a number, pad with times symbol
        if (
            lastComputeText.size > 1 &&
            lastComputed != null &&
            lastComputeText[0] == lastComputed.toDecimalString() &&
            isNumberChar(lastComputeText[1])
        ) {
            val start = listOf(lastComputeText[0], "x")
            lastComputeText = start + lastComputeText.subList(1, lastComputeText.size)
        }

        generatedHistoryItem = when {
            error != null -> HistoryItem(lastComputeText, error!!, lastComputed)
            computedValue != null -> HistoryItem(lastComputeText, computedValue!!, lastComputed)
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

        var newComputeText = item.computation

        if (item.previousResult != null) {
            computedValue = item.previousResult
            newComputeText = newComputeText.subList(1, newComputeText.size)
        }

        computeText = newComputeText
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
