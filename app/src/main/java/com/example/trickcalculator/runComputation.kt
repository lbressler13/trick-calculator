package com.example.trickcalculator

import android.util.Log
import com.example.trickcalculator.utils.*

// parse string list and compute mathematical expression, if possible
fun runComputation(
    computeText: StringList,
    firstRoundOps: StringList,
    secondRoundOps: StringList,
    performSingleOp: OperatorFunction,
    numbersOrder: IntList? = null
): Int {
    if (!validateComputeText(computeText, firstRoundOps + secondRoundOps)) {
        throw Exception("Err: Syntax Error")
    }
    Log.e(null, "validated")

    val text: StringList = if (validateNumbersOrder(numbersOrder)) {
        replaceNumbers(computeText, numbersOrder!!)
    } else {
        computeText
    }

    return try {
        parseText(text, firstRoundOps, secondRoundOps, performSingleOp)
    } catch (e: ArithmeticException) {
        throw Exception("Err: Divide by 0")
    }
}

private fun validateComputeText(computeText: StringList, ops: StringList): Boolean {
    // must start and end w/ number
    if (isOperator(computeText.first(), ops) || isOperator(computeText.last(), ops)) {
        return false
    }

    var lastType = ""
    var openParenCount = 0

    for (element in computeText) {
        val currentType: String? = when {
            isOperator(element, ops) -> "operator"
            isInt(element) -> "number"
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

private fun validateNumbersOrder(order: IntList?): Boolean {
    return order != null
            && order.joinToString("") != "0123456789"
            && order.sorted().joinToString("") == "0123456789"
}

// map digits to use values in numbers order
private fun replaceNumbers(text: StringList, numbersOrder: IntList): StringList {
    return text.map {
        if (!isInt(it)) {
            it
        } else {
            it.map { c ->
                val index = Integer.parseInt(c.toString())
                numbersOrder[index].toString()
            }.joinToString("")
        }
    }
}

// run calculation by parsing text and performing operations
private fun parseText(
    computeText: StringList,
    firstRoundOps: StringList,
    secondRoundOps: StringList,
    performSingleOp: OperatorFunction
): Int {
    var total = 0
    var currentOperator: String? = null
    val afterFirstRound: StringList = if (firstRoundOps.isEmpty()) {
        computeText
    } else {
        parseFirstRound(computeText, firstRoundOps, performSingleOp)
    }

    // run second round operators (probably addition and subtraction)
    for (element in afterFirstRound) {
        when {
            isOperator(element, secondRoundOps) -> currentOperator = element
            currentOperator == null -> total = Integer.parseInt(element)
            else -> {
                val currentVal: Int = Integer.parseInt(element)
                total = performSingleOp(total, currentVal, currentOperator)
            }
        }
    }

    return total
}

// run first round of operators (probably multiply and divide)
private fun parseFirstRound(
    computeText: StringList,
    firstRoundOps: StringList,
    performSingleOp: OperatorFunction
): StringList {
    val simplifiedList: MutableList<String> = mutableListOf()

    var index = 0

    while (index < computeText.size) {
        val element = computeText[index]

        if (element !in firstRoundOps) {
            simplifiedList.add(element)
            index++
        } else {
            // don't have to worry about out of bounds or parse errors b/c of validation
            val leftVal: Int = Integer.parseInt(simplifiedList.last())
            val rightVal: Int = Integer.parseInt(computeText[index + 1])
            val result = performSingleOp(leftVal, rightVal, element)
            val lastIndex = simplifiedList.lastIndex

            simplifiedList[lastIndex] = result.toString()

            // skip past next value, which was already used as rightValue
            index += 2
        }
    }

    return simplifiedList
}

private fun isOperator(element: String, ops: StringList): Boolean {
    return element in ops
}

private fun isParen(element: String): Boolean {
    return element == "(" || element == ")"
}
