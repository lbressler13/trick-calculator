package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.IntList
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isNumber

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
