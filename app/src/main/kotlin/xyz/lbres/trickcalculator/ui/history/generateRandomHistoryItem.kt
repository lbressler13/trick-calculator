package xyz.lbres.trickcalculator.ui.history

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.int.ext.ifZero
import xyz.lbres.kotlinutils.int.ext.isZero
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.list.WeightedList
import xyz.lbres.kotlinutils.random.ext.nextBoolean
import xyz.lbres.kotlinutils.random.ext.nextFromWeightedList
import xyz.lbres.trickcalculator.SharedValues.random
import xyz.lbres.trickcalculator.compute.isOperator
import xyz.lbres.trickcalculator.ext.random.seededRandom
import xyz.lbres.trickcalculator.utils.isNumber

private typealias MStringList = MutableList<String>

/**
 * Maximum number of numbers in a generated computation
 */
private const val maxCompLength = 8

/**
 * Probabilities used in main generate function
 */
private const val probabilityErrorMessage = 0.1f
private const val probabilitySyntaxError = 0.1f
private const val probabilityParens = 0.2f

/**
 * Values for generating an ExactFraction to use as a the result in a HistoryItem
 */
private const val probabilityWholeNumberResult = 0.6f
private val weightedRangesResult = listOf(
    Pair(0 until 500, 0.2f),
    Pair(500 until 10000, 0.3f),
    Pair(10000 until Int.MAX_VALUE / 2, 0.25f),
    Pair(Int.MAX_VALUE / 2..Int.MAX_VALUE, 0.25f)
)

/**
 * Values for generating an ExactFraction to use as a term in the generated computation
 */
private const val probabilityWholeNumberComputation = 0.85f
private val weightedRangesComputation = listOf(
    Pair(0 until 1000, 0.5f),
    Pair(1000 until 100000, 0.2f),
    Pair(100000 until Int.MAX_VALUE / 2, 0.2f),
    Pair(Int.MAX_VALUE / 2..Int.MAX_VALUE, 0.1f)
)

private val operators = listOf("+", "-", "x", "/", "^")

/**
 * Generate a history item with a random computation string and result or error message
 *
 * @return [HistoryItem]: randomly generated item
 */
fun generateRandomHistoryItem(): HistoryItem {
    // show error message instead of result
    val showError = random.nextBoolean(probabilityErrorMessage)

    val length = (1..maxCompLength).seededRandom()
    val computation = generateComputation(length)

    if (showError) {
        return HistoryItem(computation, generateErrorMessage())
    }

    val result = generateExactFraction(weightedRangesResult, probabilityWholeNumberResult)
    return HistoryItem(computation, result)
}

/**
 * Generate a computation list, consisting of numbers, operators, and possibly parentheses.
 * May also include a syntax error.
 *
 * @param length [Int]: how many numbers should be in the computation. Assumed to be greater than 0
 * @return [StringList]
 */
private fun generateComputation(length: Int): StringList {
    val totalLength = length * 2 - 1

    val computation = MutableList(totalLength) {
        if (it % 2 == 0) {
            val ef = generateExactFraction(
                weightedRangesComputation,
                probabilityWholeNumberComputation,
                allowNegative = false
            )
            ef.toDecimalString(5)
        } else {
            operators.seededRandom()
        }
    }

    if (random.nextBoolean(probabilityParens)) {
        addParens(computation)
    }

    if (random.nextBoolean(probabilitySyntaxError)) {
        addSyntaxError(computation)
    }

    return computation
}

/**
 * Generate an ExactFraction given weighted ranges and the probability of a whole number
 *
 * @param weightedRanges [List]: a list of ranges that which a numerator/denominator can be chosen from, with weights to indicate probability of choosing from each range
 * @param probabilityWholeNumber [Float]: probability that the generated number will be a whole number, with denominator 1
 * @param allowNegative [Boolean]: whether or not the number can be negative. Defaults to false
 * @return [ExactFraction]: a number generated randomly using the specified parameters
 */
private fun generateExactFraction(
    weightedRanges: WeightedList<IntRange>,
    probabilityWholeNumber: Float,
    allowNegative: Boolean = true
): ExactFraction {
    val wholeNumber = random.nextBoolean(probabilityWholeNumber)
    val negative = allowNegative && random.nextBoolean()

    val numeratorRange = random.nextFromWeightedList(weightedRanges)
    var numerator = numeratorRange.seededRandom()
    if (negative) {
        numerator *= -1
    }
    if (wholeNumber) {
        return ExactFraction(numerator)
    }

    val denominatorRange = random.nextFromWeightedList(weightedRanges)
    val denominator = denominatorRange.seededRandom().ifZero { 1 }
    return ExactFraction(numerator, denominator)
}

/**
 * Generates a random error message
 *
 * @return [String]: an error message
 */
private fun generateErrorMessage(): String {
    val messages = listOf(
        "Syntax error",
        "Divide by zero",
        "Number overflow exception"
    )

    val message = messages.seededRandom()

    if (message == "Number overflow exception") {
        val overflow = (Long.MIN_VALUE..Long.MAX_VALUE).seededRandom()
        return "Number overflow exception on $overflow"
    }

    return message
}

/**
 * Add a single set of parentheses to the given computation string, if string has size greater than 1
 *
 * @param computation [MStringList]: existing computation.
 * Assumed to contain only numbers and operators, without any syntax errors.
 */
private fun addParens(computation: MStringList) {
    if (computation.size > 1) {
        var startIndex = (0 until computation.size - 1).seededRandom()
        // must be inserted before a number
        if (isOperator(computation[startIndex], operators)) {
            startIndex--
        }
        computation.add(startIndex, "(")

        var endIndex = (startIndex + 1 until computation.size).seededRandom()
        // must be inserted after a number
        if (isNumber(computation[endIndex])) {
            endIndex++
        }
        // avoid having parens around a single number, if possible
        if (endIndex == startIndex + 2) {
            if (endIndex + 2 <= computation.size) {
                endIndex += 2
            }
        }
        computation.add(endIndex, ")")
    }
}

/**
 * Add a single syntax error to the computation
 *
 * @param computation [MStringList]: existing computation
 */
private fun addSyntaxError(computation: MStringList) {
    val errorType = listOf(
        "singleParen",
        "extraOperator",
        "emptyParens",
        "extraDecimal"
    ).seededRandom()

    when (errorType) {
        "singleParen" -> addUnmatchedParen(computation)
        "extraOperator" -> addOperatorSyntaxError(computation)
        "emptyParens" -> {
            val index = (0..computation.size).seededRandom()
            computation.add(index, "()")
        }
        "extraDecimal" -> addDecimalSyntaxError(computation)
    }
}

/**
 * Add an error to the computation string to create a syntax error
 *
 * @param computation [MStringList]: existing computation
 */
private fun addOperatorSyntaxError(computation: MStringList) {
    val index = (0..computation.size).seededRandom()
    var operator = operators.seededRandom()

    // putting - at the start of the operation would look like a negative, not a syntax error
    if (index.isZero() && operator == "-") {
        operator = listOf("+", "x", "/", "^").seededRandom()
    }

    // check for same operator twice in a row
    if (!index.isZero() && index != computation.size) {
        val current = computation[index]
        val previous = computation[index - 1]
        val adjacentOperator = when {
            isOperator(current, operators) -> current
            isOperator(previous, operators) -> previous
            else -> ""
        }

        // select new operator to avoid adjacent duplicates
        if (adjacentOperator == operator) {
            operator = operators.filter { it != operator }.seededRandom()
        }
    }
    computation.add(index, operator)
}

/**
 * Add a decimal point to the computation string to create a syntax error
 *
 * @param computation [MStringList]: existing computation
 */
private fun addDecimalSyntaxError(computation: MStringList) {
    var index = (0..computation.size).seededRandom()
    if (index != computation.size) {
        val item = computation[index]
        if (isNumber(item) && !item.contains(".")) {
            index++
        }
    }
    computation.add(index, ".")
}

/**
 * Add a single unmatched paren to the computation to create a syntax error
 *
 * @param computation [MStringList]: existing computation, which may already contain matched parentheses
 */
private fun addUnmatchedParen(computation: MStringList) {
    var index = (0..computation.size).seededRandom()
    var paren = listOf("(", ")").seededRandom()

    // biased in favor of putting paren next to the existing paren, if possible
    val existingIndex = computation.indexOf(paren)
    if (existingIndex != -1) {
        val indices = listOf(
            Pair(existingIndex, 0.7f),
            Pair(index, 0.3f)
        )
        index = random.nextFromWeightedList(indices)
    }

    // place opening paren in front of a number
    if (index != existingIndex && index != computation.size && isNumber(computation[index])) {
        paren = "("
    }
    // place closing paren after a number
    else if (index != existingIndex) {
        paren = ")"
    }

    computation.add(index, paren)
}
