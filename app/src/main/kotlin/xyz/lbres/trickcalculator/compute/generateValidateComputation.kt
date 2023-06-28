package xyz.lbres.trickcalculator.compute

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.list.mutablelist.ext.popRandom
import xyz.lbres.trickcalculator.utils.isNumber
import xyz.lbres.trickcalculator.utils.isNumberChar

/**
 * Types
 */
private const val NUMBER = "number"
private const val OPERATOR = "operator"
private const val LPAREN = "lparen"
private const val RPAREN = "rparen"

private val parenCounts = mapOf(LPAREN to 1, RPAREN to -1)
private val syntaxError = Exception("Syntax error")

/**
 * Class to store all values used in computation
 */
private data class ComputeData(
    val computeText: MutableList<String> = mutableListOf(),
    var lastType: String = "",
    var currentType: String = "",
    var currentNumber: String = "",
    var openParenCount: Int = 0,
    // track decimal state in case applyDecimals = false
    var currentDecimal: Boolean = false, // if current num already has a decimal, used to check if there are multiple decimals
    var lastDecimal: Boolean = false // if the most recent element was a decimal, used to check a number ends with a decimal
)

/**
 * Validate computation text, combine adjacent digits/decimals to form numbers,
 * and perform modifications related to number order and application of parens and/or decimals.
 *
 * Validations:
 * - Each element has length 1
 * - Doesn't start or end with operator
 * - All values are numbers, operators, or parens
 * - Parentheses are matched
 * - No successive operators
 * - Operators are not the first or last value within a set of parens
 *
 * Assumptions:
 * - If the number order is not `null`, it has already passed validation
 * - If the initial value is not `null`, any necessary number substitutions have already been performed on it
 *
 * @param initialValue [ExactFraction]: the previously computed value, if being used as the start of the computation. Can be `null`
 * @param splitText [StringList]: list of single-character values to combine, excluding the initial value
 * @param ops [List]: list of string values recognized as operators
 * @param numbersOrder [IntList]: list of numbers containing the values 0..9 in any other order, or `null`
 * @param applyParens [Boolean]: whether or not parentheses should be applied
 * @param applyDecimals [Boolean]: whether or not decimals points should be applied.
 * This does not impact any decimals in the initial value.
 * @param shuffleComputation [Boolean]: whether or not the order of numbers and order of ops should be shuffled.
 * @return [StringList]: modified list with adjacent digits/decimals combined into single numbers,
 * and with any of the specified modifications for number order, applying parens, and applying decimals.
 * The relative position of numbers, operators, and parens is unchanged in the returned value.
 */
fun generateAndValidateComputeText(
    initialValue: ExactFraction?,
    splitText: StringList,
    ops: StringList,
    numbersOrder: IntList?,
    applyParens: Boolean,
    applyDecimals: Boolean,
    shuffleComputation: Boolean
): StringList {
    val data = ComputeData()

    // empty compute text or starting with operator
    when {
        splitText.isEmpty() && initialValue == null -> return emptyList()
        splitText.isEmpty() -> return listOf(initialValue!!.toEFString())
        initialValue == null && isOperator(splitText[0], ops) -> throw syntaxError
    }

    if (initialValue != null) {
        addInitialValue(data, initialValue, splitText)
    }

    // loop over all elements
    for (element in splitText) {
        if (element.length != 1) {
            throw syntaxError
        }

        data.currentType = getTypeOf(element, ops) ?: throw syntaxError

        if (data.currentType != NUMBER && data.currentNumber.isNotEmpty()) {
            addCurrentNumber(data)
        }

        data.openParenCount += parenCounts.getOrDefault(data.currentType, 0)
        when {
            nonNumberSyntaxError(data) -> throw syntaxError
            data.currentType == NUMBER -> addDigit(data, element, applyDecimals, numbersOrder)
            else -> addNonNumber(data, element, applyParens)
        }
    }

    // add remaining number
    if (data.currentNumber.isNotEmpty()) {
        addCurrentNumber(data)
    }

    // check syntax error at end
    val endsWithOperator = data.lastType == OPERATOR && data.currentNumber.isEmpty()
    if (data.openParenCount != 0 || endsWithOperator) {
        throw syntaxError
    }

    if (shuffleComputation) {
        return getShuffledComputation(data.computeText, ops)
    }

    return data.computeText
}

/**
 * Add the previously computed value to the start of the computation
 *
 * @param data [ComputeData]: data about current state of computation
 * @param initialValue [ExactFraction]: previously computed value, used as the first element in computation
 * @param initialText [StringList]: following compute text
 */
private fun addInitialValue(data: ComputeData, initialValue: ExactFraction, initialText: StringList) {
    data.computeText.add(initialValue.toEFString())
    data.lastType = NUMBER

    // add times between initial val and first num
    if (initialText.isNotEmpty() && isNumberChar(initialText[0])) {
        data.computeText.add("x")
    }
}

/**
 * Add a digit or decimal to the current number
 *
 * @param data [ComputeData]: data about current state of computation
 * @param element [String]: element to add
 * @param applyDecimals [Boolean]: whether or not decimals should be applied
 * @param numbersOrder [IntList]?: list of numbers containing the values 0..9 in any other order, or `null`
 */
private fun addDigit(data: ComputeData, element: String, applyDecimals: Boolean, numbersOrder: IntList?) {
    when {
        element == "." && data.currentDecimal -> throw syntaxError
        element == "." -> {
            if (applyDecimals) {
                data.currentNumber += element
            }
            // update decimals even when not applied, to check for syntax errors
            data.currentDecimal = true
            data.lastDecimal = true
        }
        else -> {
            data.currentNumber += digitFromNumbersOrder(element, numbersOrder)
            data.lastDecimal = false
        }
    }
}

/**
 * Map an element to the corresponding value in the numbers order. Returns [element] if the order is null.
 *
 * @param element [String]: value to map
 * @param numbersOrder [IntList]?: list of numbers containing the values 0..9 in any other order, or `null`
 */
private fun digitFromNumbersOrder(element: String, numbersOrder: IntList?): String {
    if (numbersOrder == null) {
        return element
    }

    return try {
        val index = element.toInt()
        val digit = numbersOrder[index]
        digit.toString()
    } catch (_: NumberFormatException) {
        element
    }
}

/**
 * Add an operator or paren to the compute text
 *
 * @param data [ComputeData]: data about current state of computation
 * @param element [String]: element to add
 * @param applyParens [Boolean]: whether or not parens should be applied
 */
private fun addNonNumber(data: ComputeData, element: String, applyParens: Boolean) {
    // add times between preceding num and paren, or between adjacent parens
    if ((data.lastType == NUMBER && data.currentType == LPAREN) || (data.lastType == RPAREN && data.currentType == LPAREN)) {
        data.computeText.add("x")
    }

    if ((data.currentType != LPAREN && data.currentType != RPAREN) || applyParens) {
        data.computeText.add(element)
    }

    data.lastType = data.currentType
    data.lastDecimal = false
}

/**
 * Add the current number to the compute text
 *
 * @param data [ComputeData]: data about current state of computation
 */
private fun addCurrentNumber(data: ComputeData) {
    if (data.currentNumber.isNotEmpty() && data.lastDecimal) {
        throw syntaxError
    }

    if (data.currentNumber.isNotEmpty()) {
        // add times between number and preceding paren
        if (data.lastType == RPAREN) {
            data.computeText.add("x")
        }

        data.computeText.add(data.currentNumber)
        data.currentNumber = ""
        data.currentDecimal = false

        data.lastType = NUMBER
    }
}

/**
 * Get the type of an element
 *
 * @param element [String]: value to get type of
 * @param ops [StringList]: list of valid operators
 * @return [String]?: the element's type, or `null` if it matches no valid type
 */
private fun getTypeOf(element: String, ops: StringList): String? {
    return when {
        isOperator(element, ops) -> OPERATOR
        element == "(" -> LPAREN
        element == ")" -> RPAREN
        isNumberChar(element) -> NUMBER
        else -> null
    }
}

/**
 * Determine if there is a syntax error due to placement of operators or parens, at the current position in the text
 *
 * @param data [ComputeData]: data about current state of computation
 * @return [Boolean]: `true` if there is an error, `false` otherwise
 */
private fun nonNumberSyntaxError(data: ComputeData): Boolean {
    val operatorInsideParens = (data.lastType == LPAREN && data.currentType == OPERATOR) || (data.lastType == OPERATOR && data.currentType == RPAREN)
    val doubleOperators = data.lastType == OPERATOR && data.currentType == OPERATOR
    val emptyParens = data.lastType == LPAREN && data.currentType == RPAREN

    return data.openParenCount < 0 || operatorInsideParens || doubleOperators || emptyParens
}

/**
 * Shuffle the order of the numbers and order of ops in compute text.
 * The relative positions of parens, numbers, and ops remains the same, but the values may change.
 *
 * @param computeText [StringList]: list of strings to shuffle, containing numbers, operators, and parens
 * @param ops [StringList]: list of string values recognized as operators
 * @return [StringList]: a list containing the same elements as the computeText, with the orders of numbers and operators shuffled
 */
// public for testing purposes
fun getShuffledComputation(computeText: StringList, ops: StringList): StringList {
    val numbers = computeText.filter { isNumber(it) }.toMutableList()
    val operators = computeText.filter { isOperator(it, ops) }.toMutableList()

    val newText = computeText.map {
        when {
            isOperator(it, ops) -> operators.popRandom()!!
            isNumber(it) -> numbers.popRandom()!!
            else -> it
        }
    }

    return newText
}
