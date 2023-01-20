package xyz.lbres.trickcalculator.compute

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.list.mutablelist.ext.popRandom
import xyz.lbres.trickcalculator.utils.isNumber
import xyz.lbres.trickcalculator.utils.isNumberChar

private const val NUMBER = "number"
private const val OPERATOR = "operator"
private const val LPAREN = "lparen"
private const val RPAREN = "rparen"

private val syntaxError = Exception("Syntax error")

private val computeText = mutableListOf<String>()
private var lastType = ""
private var currentType = ""
private var currentNumber = ""
private var currentDecimal = false
private var lastDecimal = false
private var openParenCount = 0

/**
 * Validate computation text, combine adjacent digits/decimals to form numbers,
 * and perform modifications related to number order and application of parens and/or decimals.
 *
 * Validations:
 * - Each element has length 1
 * - Doesn't start or end with operator
 * - All values are number, operator, or paren
 * - Parentheses are matched
 * - No successive operators
 * - Operators are not the first or last value within a set of parens
 *
 * Assumptions:
 * - If the number order is not null, it has already passed validation
 * - If the initial value is not null, any necessary number substitutions have already been performed on it
 *
 * @param initialValue [ExactFraction]: the previously computed value, if being used as the start of the computation
 * @param splitText [StringList]: list of single-character values to combine, excluding initialValue
 * @param ops [List]: list of string values recognized as operators
 * @param numbersOrder [IntList]: list of numbers, containing the values 0..9 in any other order
 * @param applyParens [Boolean]: whether or not parentheses should be applied
 * @param applyDecimals [Boolean]: whether or not decimals points should be applied.
 * This does not impact any decimals in the initial value.
 * @param shuffleComputation [Boolean]: whether or not the order of numbers and order of ops should be shuffled.
 * @return [StringList]: modified list with adjacent digits/decimals combined into single numbers,
 * and with any of the specified modifications for number order, applying parens, and applying decimals.
 * The relative position of numbers, operators, and parens is unchanged in the returned value.
 * @throws Exception: if validation fails
 */
fun generateAndValidateComputeText(
    initialValue: ExactFraction?,
    splitText: StringList,
    ops: StringList,
    numbersOrder: IntList?, // make this nullable so we don't always have to get index
    applyParens: Boolean,
    applyDecimals: Boolean,
    shuffleComputation: Boolean
): StringList {
    resetGlobalVars()

    // empty compute text
    when {
        splitText.isEmpty() && initialValue == null -> return emptyList()
        splitText.isEmpty() -> return listOf(initialValue!!.toEFString())
    }

    if (initialValue != null) {
        addInitialValue(initialValue, splitText)
    }

    // loop over all elements
    for (element in splitText) {
        if (element.length != 1) {
            throw syntaxError
        }

        currentType = getTypeOf(element, ops) ?: throw syntaxError

        if (currentType != NUMBER && currentNumber.isNotEmpty()) {
            addCurrentNumber()
        }

        updateParenCount()
        if (nonNumberSyntaxError(openParenCount)) {
            throw syntaxError
        }

        if (currentType == NUMBER) {
            addDigit(element, applyDecimals, numbersOrder)
        } else {
            addNonNumber(element, applyParens)
        }
    }

    if (currentNumber.isNotEmpty()) {
        addCurrentNumber()
    }

    val startsWithOperator = computeText.isNotEmpty() && isOperator(computeText[0], ops)
    val endsWithOperator = lastType == OPERATOR && currentNumber.isEmpty()
    if (openParenCount != 0 || startsWithOperator || endsWithOperator) {
        throw syntaxError
    }

    if (shuffleComputation) {
        return getShuffledComputation(computeText, ops)
    }

    return computeText
}

/**
 * Clear saved compute text and reset values of global variables
 */
private fun resetGlobalVars() {
    computeText.clear()
    lastType = ""
    currentType = ""
    currentNumber = ""
    currentDecimal = false
    lastDecimal = false
    openParenCount = 0
}

/**
 * Add the previously computed value to the start of the computation
 *
 * @param initialValue [ExactFraction]: previously computed value, used as the first element in computation
 * @param initialText [StringList]: following compute text
 */
private fun addInitialValue(initialValue: ExactFraction, initialText: StringList) {
    computeText.add(initialValue.toEFString())
    lastType = NUMBER

    // add multiplication between initial val and first num
    if (initialText.isNotEmpty() && isNumberChar(initialText[0])) {
        computeText.add("x")
    }
}

/**
 * Add a digit or decimal to the current number
 *
 * @param element [String]: element to add
 * @param applyDecimals [Boolean]: whether or not decimals points should be applied
 * @param numbersOrder [IntList]?: list of numbers, containing the values 0..9 in any other order, or null
 */
private fun addDigit(element: String, applyDecimals: Boolean, numbersOrder: IntList?) {
    when {
        element == "." && currentDecimal -> throw syntaxError
        element == "." -> {
            if (applyDecimals) {
                currentNumber += element
            }
            currentDecimal = true // gets counted even when not applied, to check for syntax errors
            lastDecimal = true // tracked separately for use in syntax errors
        }
        else -> {
            currentNumber += digitFromNumbersOrder(element, numbersOrder)
            lastDecimal = false
        }
    }
}

/**
 * Map an element to the corresponding value in the numbers order. Returns [element] if the order is null.
 *
 * @param element [String]: value to map
 * @param numbersOrder [IntList]?: list containing numbers 0..9, or null
 */
private fun digitFromNumbersOrder(element: String, numbersOrder: IntList?): String {
    if (numbersOrder == null) {
        return element
    }

    val index = element.toInt()
    val digit = numbersOrder[index]
    return digit.toString()
}

/**
 * Add an operator or paren to the compute text
 *
 * @param element [String]: element to add
 * @param applyParens [Boolean]: whether or not parens should be applied
 */
private fun addNonNumber(element: String, applyParens: Boolean) {
    // add times between preceding num and paren, or between adjacent parens
    if ((lastType == NUMBER && currentType == LPAREN) || (lastType == RPAREN && currentType == LPAREN)) {
        computeText.add("x")
    }

    if ((currentType != LPAREN && currentType != RPAREN) || applyParens) {
        computeText.add(element)
    }

    lastType = currentType
    lastDecimal = false
}

/**
 * Add the current number to the compute text
 */
private fun addCurrentNumber() {
    if (currentNumber.isNotEmpty() && lastDecimal) {
        throw syntaxError
    }

    if (currentNumber.isNotEmpty()) {
        // add multiplication between number and preceding paren
        if (lastType == RPAREN) {
            computeText.add("x")
        }
        computeText.add(currentNumber)
        currentNumber = ""
        currentDecimal = false

        lastType = NUMBER
    }
}

/**
 * Get the type of an element
 *
 * @param element [String]: value to get type of
 * @param ops [StringList]: list of valid operators
 * @return [String]?: the element's type, or null if it matches no valid type
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
 * Increment or decrement the paren count based on the current type
 */
private fun updateParenCount() {
    if (currentType == LPAREN) {
        openParenCount++
    } else if (currentType == RPAREN) {
        openParenCount--
    }
}

/**
 * Determine if there is a syntax error due to placement of operators or parens
 *
 * @param openParenCount [Int]: number of current open sets of parens
 * @return [Boolean]: `true` if there is an error, `false` otherwise
 */
private fun nonNumberSyntaxError(openParenCount: Int): Boolean {
    val operatorInsideParens = (lastType == LPAREN && currentType == OPERATOR) || (lastType == OPERATOR && currentType == RPAREN)
    val doubleOperators = lastType == OPERATOR && currentType == OPERATOR
    val emptyParens = lastType == LPAREN && currentType == RPAREN

    return openParenCount < 0 || operatorInsideParens || doubleOperators || emptyParens
}

/**
 * Shuffle the order of the numbers and order of ops in compute text.
 * The relative positions of parens, numbers, and ops remains the same, but the values may change.
 *
 * @param computeText [StringList]: list of strings to shuffle, containing numbers, operators, and parens
 * @param ops [StringList]: list of string values recognized as operators
 * @return [StringList]: a list containing the same elements as the computeText, with the orders of numbers and operators shuffled
 */
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
