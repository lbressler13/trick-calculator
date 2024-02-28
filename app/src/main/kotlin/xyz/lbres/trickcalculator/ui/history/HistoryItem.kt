package xyz.lbres.trickcalculator.ui.history

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.list.ext.copyWithReplacement

/**
 * Information about a single computation and its result
 *
 * @param computation [StringList]: input computation
 * @param result [ExactFraction]?: result of computation if no error was thrown
 * @param previousResult [ExactFraction]?: result that is used as the first term of the computation
 * @param error [String]?: error message if computation threw an error
 */
class HistoryItem private constructor(computation: StringList, val result: ExactFraction?, val error: String?, val previousResult: ExactFraction?) {
    val computation: StringList

    init {
        this.computation = if (result != null && computation.isNotEmpty() && ExactFraction.isEFString(computation[0])) {
            // parse EF-formatted value into decimal string
            val decimal = ExactFraction(computation[0]).toDecimalString(5)
            computation.copyWithReplacement(0, decimal)
        } else {
            computation
        }
    }

    /**
     * Constructor for HistoryItem resulting from an error
     */
    constructor(computation: StringList, error: String, previousResult: ExactFraction? = null) :
        this (computation, result = null, error, previousResult)

    /**
     * Constructor for HistoryItem for a successful computation
     */
    constructor(computation: StringList, result: ExactFraction, previousResult: ExactFraction? = null) :
        this (computation, result, error = null, previousResult)

    override fun toString(): String {
        if (error != null) {
            return "HistoryItem[$computation, err: $error]"
        }

        return "HistoryItem[$computation, res: $result]"
    }
}
