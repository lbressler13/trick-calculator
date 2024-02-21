package xyz.lbres.trickcalculator.ui.history

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.list.ext.copyWithReplacement

/**
 * Information about a single computation and its result
 */
class HistoryItem private constructor(computation: StringList, result: ExactFraction?, error: String?, previousResult: ExactFraction?) {
    /**
     * Input computation
     */
    val computation: StringList

    /**
     * Result of computation if no error was thrown
     */
    val result: ExactFraction?

    /**
     * Result that is used as the first term of the computation
     */
    val previousResult: ExactFraction?

    /**
     * Error message if computation threw an error
     */
    val error: String?

    init {
        this.computation = if (result != null && computation.isNotEmpty() && ExactFraction.isEFString(computation[0])) {
            // parse EF-formatted value into decimal string
            val decimal = ExactFraction(computation[0]).toDecimalString(5)
            val newComputation = computation.copyWithReplacement(0, decimal)
            newComputation
        } else {
            computation
        }

        this.result = result
        this.error = error
        this.previousResult = previousResult
    }

    /**
     * Constructor for HistoryItem resulting from an error
     */
    constructor(computation: StringList, error: String, previousResult: ExactFraction? = null) :
        this (computation, null, error, previousResult)

    /**
     * Constructor for HistoryItem for a successful computation
     */
    constructor(computation: StringList, result: ExactFraction, previousResult: ExactFraction? = null) :
        this (computation, result, null, previousResult)

    override fun toString(): String {
        if (error != null) {
            return "HistoryItem[$computation, err: $error]"
        }

        return "HistoryItem[$computation, res: $result]"
    }
}
