package xyz.lbres.trickcalculator.ui.history

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.exactfraction.checkIsEFString
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.list.ext.copyWithReplacement

/**
 * Information about a single computation and its result
 */
class HistoryItem {
    /**
     * Input computation
     */
    val computation: StringList

    /**
     * Result of computation if no error was thrown
     */
    val result: ExactFraction?

    val previousResult: ExactFraction?

    /**
     * Error message if computation threw an error
     */
    val error: String?

    constructor(computation: StringList, error: String, previousResult: ExactFraction? = null) {
        this.computation = computation
        this.result = null
        this.error = error
        this.previousResult = previousResult
    }

    constructor(computation: StringList, result: ExactFraction, previousResult: ExactFraction? = null) {
        // parse EF-formatted value into decimal string
        if (computation.isNotEmpty() && checkIsEFString(computation[0])) {
            val decimal = ExactFraction(computation[0]).toDecimalString(5)
            val newComputation = computation.copyWithReplacement(0, decimal)
            this.computation = newComputation
        } else {
            this.computation = computation
        }
        this.result = result
        this.error = null
        this.previousResult = previousResult
    }

    override fun toString(): String {
        if (error != null) {
            return "HI[$computation, err: $error]"
        }

        return "HI[$computation, res: $result]"
    }
}
