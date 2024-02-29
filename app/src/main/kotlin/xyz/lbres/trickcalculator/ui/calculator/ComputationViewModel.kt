package xyz.lbres.trickcalculator.ui.calculator

import androidx.lifecycle.ViewModel
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.list.ext.copyWithoutLast
import xyz.lbres.trickcalculator.ui.history.HistoryItem

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
     * Set error or computed result, update compute text, and create history item based on result.
     *
     * @param newError [String]?: error result, null if no error was thrown
     * @param newComputed [ExactFraction]?: computed result, null if result was not computed successfully
     * @param clearOnError [Boolean]: if compute data should be cleared in event of an error. Defaults to `false`
     * @return [HistoryItem]?: new history item based on previous values and new result/error
     */
    fun setResult(newError: String?, newComputed: ExactFraction?, clearOnError: Boolean = false): HistoryItem? {
        val lastComputeText = computeText
        val lastComputed = computedValue
        error = newError
        if (newComputed != null) {
            computedValue = newComputed
            computeText = emptyList()
        }

        val historyItem = generateHistoryItem(lastComputed, lastComputeText)

        when {
            newComputed != null -> computeText = emptyList()
            newError != null && clearOnError -> resetComputeData(clearError = false)
        }

        return historyItem
    }

    /**
     * Generate last history item based on most recent error or computation
     *
     * @return [HistoryItem]?: the generated item
     */
    private fun generateHistoryItem(lastComputed: ExactFraction?, lastComputeText: StringList): HistoryItem? {
        var updatedComputeText = lastComputeText

        // add computed text to start of computation
        if (lastComputed != null) {
            val decimalString = lastComputed.toDecimalString()
            updatedComputeText = listOf(decimalString) + updatedComputeText
        }

        return when {
            error != null -> HistoryItem(updatedComputeText, error!!, lastComputed)
            computedValue != null -> HistoryItem(updatedComputeText, computedValue!!, lastComputed)
            else -> null
        }
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
        computeText = emptyList()
        computedValue = null

        if (clearError) {
            error = null
        }
    }
}
