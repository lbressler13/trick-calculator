package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.IntList
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isNumber

fun validateComputeText(computeText: StringList, ops: StringList): Boolean {
    // must start and end w/ number
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
            (lastType == currentType && !currentType.endsWith("paren")) || // repeated num or op
            (lastType == "lparen" && currentType == "rparen") // empty parens
        ) {
            return false
        }

        lastType = currentType
    }

    return openParenCount == 0
}

fun validateNumbersOrder(order: IntList?): Boolean {
    return order != null
            && order.joinToString("") != "0123456789"
            && order.sorted().joinToString("") == "0123456789"
}
