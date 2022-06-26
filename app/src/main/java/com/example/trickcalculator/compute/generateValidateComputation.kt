package com.example.trickcalculator.compute

import kotlinutils.list.IntList
import kotlinutils.list.StringList
import com.example.trickcalculator.utils.isNumber
import com.example.trickcalculator.utils.isNumberChar
import exactfraction.ExactFraction
import kotlinutils.list.mutablelist.ext.popRandom

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
    val syntaxError = Exception("Syntax error")

    // empty compute text
    if (splitText.isEmpty() && initialValue == null) {
        return listOf()
    }

    if (splitText.isEmpty()) {
        return listOf(initialValue!!.toEFString())
    }

    val computeText = mutableListOf<String>()
    var lastType = ""
    var currentType = ""
    var currentNumber = ""
    var currentDecimal = false
    var openParenCount = 0

    if (initialValue != null) {
        computeText.add(initialValue.toEFString())
        lastType = "number"

        // add multiplication between initial val and first num
        if (splitText.isNotEmpty() && isNumberChar(splitText[0])) {
            computeText.add("x")
        }
    }

    // add the current number to the compute text
    val addCurrentNumber: () -> Unit = {
        if (currentNumber.isNotEmpty() && currentNumber.last() == '.') {
            throw syntaxError
        }

        if (currentNumber.isNotEmpty()) {
            // add multiplication between number and preceding paren
            if (lastType == "rparen") {
                computeText.add("x")
            }
            computeText.add(currentNumber)
            currentNumber = ""
            currentDecimal = false

            lastType = "number"
        }
    }

    // add operator or paren to compute text
    val addNonNumber: (String) -> Unit = {
        if (currentNumber.isNotEmpty()) {
            addCurrentNumber()
        }

        // add mult between preceding num and paren, or between adjacent parens
        if ((lastType == "number" && currentType == "lparen") ||
            (lastType == "rparen" && currentType == "lparen")
        ) {
            computeText.add("x")
        }

        if ((currentType != "lparen" && currentType != "rparen") || applyParens) {
            computeText.add(it)
        }

        lastType = currentType
    }

    // add digit or decimal to current number
    val addDigit: (String) -> Unit = {
        when {
            it == "." && currentDecimal -> throw syntaxError
            it == "." -> {
                if (applyDecimals) {
                    currentNumber += it
                }
                currentDecimal = true // gets counted even when not applied, to check for syntax errors
            }
            numbersOrder == null -> currentNumber += it
            else -> {
                // apply numbers order
                val index = Integer.parseInt(it)
                val digit = numbersOrder[index]
                currentNumber += digit.toString()
            }
        }
    }

    // loop over all elements
    for (element in splitText) {
        if (element.length != 1) {
            throw syntaxError
        }

        currentType = when {
            isOperator(element, ops) -> "operator"
            element == "(" -> "lparen"
            element == ")" -> "rparen"
            isNumberChar(element) -> "number"
            else -> throw syntaxError
        }

        if (currentType != "number" && currentNumber.isNotEmpty()) {
            lastType = "number"
        }

        if (currentType == "lparen") {
            openParenCount++
        } else if (currentType == "rparen") {
            openParenCount--
        }

        if (openParenCount < 0 ||
            (lastType == "lparen" && currentType == "operator") || // operator inside lparen
            (lastType == "operator" && currentType == "rparen") || // operator inside rparen
            (lastType == "operator" && currentType == "operator") || // double operators
            (lastType == "lparen" && currentType == "rparen") // empty parens
        ) {
            throw syntaxError
        }

        if (currentType == "number") {
            addDigit(element)
        } else {
            addNonNumber(element)
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
