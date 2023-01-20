package xyz.lbres.trickcalculator.compute

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.list.mutablelist.ext.popRandom
import xyz.lbres.trickcalculator.utils.isNumber
import xyz.lbres.trickcalculator.utils.isNumberChar

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
    // empty compute text
    when {
        splitText.isEmpty() && initialValue == null -> return emptyList()
        splitText.isEmpty() -> return listOf(initialValue!!.toEFString())
    }

    resetGlobalVars()

    if (initialValue != null) {
        addInitialValue(initialValue, splitText)
    }

    // loop over all elements
    for (element in splitText) {
        if (element.length != 1) {
            throw syntaxError
        }

        currentType = getTypeOf(element, ops) ?: throw syntaxError

        if (currentType != "number" && currentNumber.isNotEmpty()) {
            addCurrentNumber()
        }

        updateParenCount()
        if (parenSyntaxError(openParenCount, currentType, lastType)) {
            throw syntaxError
        }

        if (currentType == "number") {
            addDigit(element, applyDecimals, numbersOrder)
        } else {
            addNonNumber(element, applyParens)
        }
    }

    if (currentNumber.isNotEmpty()) {
        addCurrentNumber()
    }

    if (
        openParenCount != 0 ||
        (computeText.isNotEmpty() && isOperator(computeText[0], ops)) || // starts with operator
        (lastType == "operator" && currentNumber.isEmpty()) // ends in operator
    ) {
        throw syntaxError
    }

    if (shuffleComputation) {
        return getShuffledComputation(computeText, ops)
    }

    return computeText
}

private fun resetGlobalVars() {
    computeText.clear()
    lastType = ""
    currentType = ""
    currentNumber = ""
    currentDecimal = false
    lastDecimal = false
    openParenCount = 0
}

private fun addInitialValue(initialValue: ExactFraction, initialText: StringList) {
    computeText.add(initialValue.toEFString())
    lastType = "number"

    // add multiplication between initial val and first num
    if (initialText.isNotEmpty() && isNumberChar(initialText[0])) {
        computeText.add("x")
    }
}

// add digit or decimal to current number
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
        numbersOrder == null -> {
            currentNumber += element
            lastDecimal = false
        }
        else -> {
            // apply numbers order
            val index = element.toInt()
            val digit = numbersOrder[index]
            currentNumber += digit.toString()
            lastDecimal = false
        }
    }
}

// add operator or paren to compute text
private fun addNonNumber(element: String, applyParens: Boolean) {
    if (shouldPadWithTimes(currentType, lastType)) {
        computeText.add("x")
    }

    if ((currentType != "lparen" && currentType != "rparen") || applyParens) {
        computeText.add(element)
    }

    lastType = currentType
    lastDecimal = false
}

// add the current number to the compute text
private fun addCurrentNumber() {
    if (currentNumber.isNotEmpty() && lastDecimal) {
        throw syntaxError
    }

    if (currentNumber.isNotEmpty()) {
        // add multiplication between number and preceding paren
        if (shouldPadWithTimes("number", lastType)) {
            computeText.add("x")
        }
        computeText.add(currentNumber)
        currentNumber = ""
        currentDecimal = false

        lastType = "number"
    }
}

// add mult between preceding num and paren, or between adjacent parens, or number and preceding paren
private fun shouldPadWithTimes(currentType: String, lastType: String): Boolean {
    return (
        (lastType == "number" && currentType == "lparen") ||
            (lastType == "rparen" && currentType == "lparen") ||
            (lastType == "rparen" && currentType == "number")
        )
}

private fun getTypeOf(element: String, ops: StringList): String? {
    return when {
        isOperator(element, ops) -> "operator"
        element == "(" -> "lparen"
        element == ")" -> "rparen"
        isNumberChar(element) -> "number"
        else -> null
    }
}

private fun updateParenCount() {
    if (currentType == "lparen") {
        openParenCount++
    } else if (currentType == "rparen") {
        openParenCount--
    }
}

private fun parenSyntaxError(openParenCount: Int, currentType: String, lastType: String): Boolean {
    return (
        openParenCount < 0 ||
            (lastType == "lparen" && currentType == "operator") || // operator inside lparen
            (lastType == "operator" && currentType == "rparen") || // operator inside rparen
            (lastType == "operator" && currentType == "operator") || // double operators
            (lastType == "lparen" && currentType == "rparen")
        ) // empty parens
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
