package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.IntList
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isNumber
import com.example.trickcalculator.utils.isNumberChar
import exactfraction.ExactFraction

/**
 * Validate computation text before parsing
 *
 * Validations:
 * - Doesn't start or end with operator
 * - All values are number, operator, or paren
 * - Parentheses are matched
 * - No successive numbers or operators
 * - Operators are not the first or last value within a set of parens
 *
 * @param computeText [List]: list of string values to parse
 * @param ops [List]: list of string values recognized as operators
 * @return true if validation succeeds, false otherwise
 */
fun validateComputeText(computeText: StringList, ops: StringList): Boolean {
    if (computeText.isEmpty()) {
        return true
    }

    // cannot start or end with operator
    if (isOperator(computeText.first(), ops) || isOperator(computeText.last(), ops)) {
        return false
    }

    var lastType = ""
    var openParenCount = 0

    for (element in computeText) {
        val currentType: String? = when {
            isOperator(element, ops) -> "operator"
            isNumber(element) -> "number"
            element == "(" -> "lparen"
            element == ")" -> "rparen"
            else -> null
        }

        if (currentType == "lparen") {
            openParenCount++
        } else if (currentType == "rparen") {
            openParenCount--
        }

        if (openParenCount < 0 ||
            currentType == null || // unknown char
            (lastType == "lparen" && currentType == "operator") || // operator next to parens
            (lastType == "operator" && currentType == "rparen") ||
            (lastType == currentType && !currentType.endsWith("paren")) || // repeated num or op
            (lastType == "lparen" && currentType == "rparen") // empty parens
        ) {
            return false
        }

        lastType = currentType
    }

    return openParenCount == 0
}

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


// assumes number order has already been substituted
fun buildAndValidateComputeText(initialValue: ExactFraction?, initialText: StringList, ops: StringList): StringList {
    if (initialText.isEmpty() && initialValue == null) {
        return listOf()
    }

    if (initialText.isEmpty()) {
        return listOf(initialValue!!.toEFString())
    }

    val computeText = mutableListOf<String>()
    var lastType = ""
    var currentNumber = ""
    var currentDecimal = false
    var openParenCount = 0

    if (initialValue != null) {
        computeText.add(initialValue.toEFString())
        lastType = "number"
    }

    val errorMessage = "Syntax error"

    val addNonNumber: (String) -> Unit = {
        if (currentNumber.isNotEmpty()) {
            computeText.add(currentNumber)
            currentNumber = ""
            currentDecimal = false
        }
        computeText.add(it)
    }

    val addNumber: (String) -> Unit = {
        if (it == "." && currentDecimal) {
            throw Exception(errorMessage)
        } else {
            currentNumber += it
        }
    }

    for (element in initialText) {
        val currentType: String = when {
            isOperator(element, ops) -> "operator"
            element == "(" -> "lparen"
            element == ")" -> "rparen"
            isNumberChar(element) -> "number"
            else -> throw Exception(errorMessage)
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
            throw Exception(errorMessage)
        }

        if (currentType == "number") {
            addNumber(element)
        } else {
            addNonNumber(element)
        }

        lastType = currentType
    }

    if (currentNumber.isNotEmpty()) {
        computeText.add(currentNumber)
    }

    if (openParenCount != 0 || isOperator(computeText.last(), ops)) {
        throw Exception(errorMessage)
    }

    return computeText
}
