package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.IntList
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isNumberChar
import exactfraction.ExactFraction

/**
 * Validate computation text, and combine adjacent digits/decimals to form numbers.
 * Assumes number substitution has already happened, if necessary.
 *
 * Validations:
 * - Each element has length 1
 * - Doesn't start or end with operator
 * - All values are number, operator, or paren
 * - Parentheses are matched
 * - No successive operators
 * - Operators are not the first or last value within a set of parens
 *
 * @param initialValue [ExactFraction]: the previously computed value, if being used as the start of the computation
 * @param splitText [StringList]: list of single-character values to combine, excluding initialValue
 * @param ops [List]: list of string values recognized as operators
 * @return [StringList]: modified list with adjacent digits/decimals combined into single numbers,
 * with the relative position of numbers, operators, and parens unchanged
 * @throws Exception if validation fails
 */
//fun buildAndValidateComputeText(
//    initialValue: ExactFraction?,
//    splitText: StringList,
//    ops: StringList
//): StringList {
//    val syntaxError = Exception("Syntax error")
//
//    if (splitText.isEmpty() && initialValue == null) {
//        return listOf()
//    }
//
//    if (splitText.isEmpty()) {
//        return listOf(initialValue!!.toEFString())
//    }
//
//    val computeText = mutableListOf<String>()
//    var lastType = ""
//    var currentNumber = ""
//    var currentDecimal = false
//    var openParenCount = 0
//
//    if (initialValue != null) {
//        computeText.add(initialValue.toEFString())
//        lastType = "number"
//
//        // can't start with number if initial value is set
//        if (splitText.isNotEmpty() && isNumberChar(splitText[0])) {
//            throw syntaxError
//        }
//    }
//
//    // Add operator or paren to compute text
//    val addNonNumber: (String) -> Unit = {
//        if (currentNumber == ".") {
//            throw syntaxError
//        }
//        if (currentNumber.isNotEmpty()) {
//            computeText.add(currentNumber)
//            currentNumber = ""
//            currentDecimal = false
//        }
//        computeText.add(it)
//    }
//
//    // Add digit or decimal to current number
//    val addDigit: (String) -> Unit = {
//        when {
//            it == "." && currentDecimal -> throw syntaxError
//            it == "." -> {
//                currentDecimal = true
//                currentNumber += it
//            }
//            else -> currentNumber += it
//        }
//    }
//
//    for (element in splitText) {
//        if (element.length != 1) {
//            throw syntaxError
//        }
//
//        val currentType: String = when {
//            isOperator(element, ops) -> "operator"
//            element == "(" -> "lparen"
//            element == ")" -> "rparen"
//            isNumberChar(element) -> "number"
//            else -> throw syntaxError
//        }
//
//        if (currentType == "lparen") {
//            openParenCount++
//        } else if (currentType == "rparen") {
//            openParenCount--
//        }
//
//        if (openParenCount < 0 ||
//            (lastType == "lparen" && currentType == "operator") || // operator next to parens
//            (lastType == "operator" && currentType == "rparen") ||
//            (lastType == "operator" && currentType == "operator") || // double operators
//            (lastType == "lparen" && currentType == "rparen") // empty parens
//        ) {
//            throw syntaxError
//        }
//
//        if (currentType == "number") {
//            addDigit(element)
//        } else {
//            addNonNumber(element)
//        }
//
//        lastType = currentType
//    }
//
//    if (
//        currentNumber.endsWith(".") ||
//        openParenCount != 0 ||
//        (computeText.isNotEmpty() && isOperator(computeText[0], ops)) || // starts with operator
//        (lastType == "operator" && currentNumber.isEmpty()) // ends in operator
//    ) {
//        throw syntaxError
//    }
//
//    if (currentNumber.isNotEmpty()) {
//        computeText.add(currentNumber)
//    }
//
//    return computeText
//}

/**
 * Validate that a number order contains only the numbers 0..9, not in the sorted order
 *
 * Validations:
 * - Order is not null
 * - Order contains current digits
 * - Order is not already sorted
 *
 * @param order [List]: list of numbers, can be null
 * @return true if validation succeeds, false otherwise
 */
fun validateNumbersOrder(order: IntList?): Boolean = order != null
        && order.joinToString("") != "0123456789"
        && order.sorted().joinToString("") == "0123456789"

// assumes numbers order has been validated
// numbers order has been applied to initial value
fun buildAndValidateComputeText(
    initialValue: ExactFraction?,
    splitText: StringList,
    ops: StringList,
    numbersOrder: IntList?, // make this nullable so we don't always have to get index
    applyParens: Boolean,
    applyDecimals: Boolean
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

        // add multiplication before number
        // also gets added before lparen, which is handled in loop
        if (splitText.isNotEmpty() && isNumberChar(splitText[0])) {
            computeText.add("x")
        }
    }

    val addCurrentNumber: () -> Unit = {
        if (currentNumber.isNotEmpty() && currentNumber.last() == '.') {
            throw syntaxError
        }

        if (currentNumber.isNotEmpty()) {
            if (lastType == "rparen") {
                computeText.add("x")
            }
            computeText.add(currentNumber)
            currentNumber = ""
            currentDecimal = false

            lastType = "number"
        }
    }

    // Add operator or paren to compute text
    val addNonNumber: (String) -> Unit = {
        if (currentNumber.isNotEmpty()) {
            addCurrentNumber()
        }

        if ((lastType == "number" && currentType == "lparen") ||
            // (lastType == "rparen" && currentType == "number") ||
            (lastType == "rparen" && currentType == "lparen")
        ) {
            computeText.add("x")
        }

        if ((currentType != "lparen" && currentType != "rparen") || applyParens) {
            computeText.add(it)
        }

        lastType = currentType
    }

    // Add digit or decimal to current number
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
            (lastType == "lparen" && currentType == "operator") || // operator next to parens
            (lastType == "operator" && currentType == "rparen") ||
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
    // currentNumber.endsWith(".")  ||
        openParenCount != 0 ||
        (computeText.isNotEmpty() && isOperator(computeText[0], ops)) || // starts with operator
        (lastType == "operator" && currentNumber.isEmpty()) // ends in operator
    ) {
        throw syntaxError
    }

    return computeText
}
