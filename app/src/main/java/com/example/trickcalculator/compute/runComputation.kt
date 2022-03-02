package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.*
import java.lang.NumberFormatException

// parse string list and compute mathematical expression, if possible
fun runComputation(
    computeText: StringList,
    firstRoundOps: StringList,
    secondRoundOps: StringList,
    performSingleOp: OperatorFunction,
    numbersOrder: IntList,
    checkParens: Boolean
): Int {
    if (!validateComputeText(computeText, firstRoundOps + secondRoundOps)) {
        throw Exception("Syntax error")
    }

    var currentState: StringList = computeText

    if (validateNumbersOrder(numbersOrder)) {
        currentState = replaceNumbers(currentState, numbersOrder)
    }

    if (!checkParens) {
        currentState = stripParens(currentState)
    }


    return try {
        parseText(currentState, firstRoundOps, secondRoundOps, performSingleOp, checkParens)
    } catch (e: ArithmeticException) {
        throw Exception("Divide by 0")
    } catch (e: NumberFormatException) {
        val startIndex = e.message?.indexOf("\"")
        val endIndex = e.message?.lastIndexOf("\"")

        val newError = if (startIndex != null && endIndex != null && endIndex - startIndex > 0) {
            val symbol = e.message?.substring(startIndex + 1, endIndex)
            "Cannot parse symbol $symbol"
        } else {
            "Parse error"
        }

        throw Exception(newError)
    }
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
    performSingleOp: OperatorFunction,
    checkParens: Boolean
): Int {
    var total = 0
    var currentOperator: String? = null

    var currentState = computeText

    if (checkParens) {
        currentState = parseParens(computeText, firstRoundOps, secondRoundOps, performSingleOp)
    }

    if (firstRoundOps.isNotEmpty()) {
        currentState = parseFirstRound(currentState, firstRoundOps, performSingleOp)
    }

    // run second round operators (probably addition and subtraction)
    for (element in currentState) {
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

private fun parseParens(
    computeText: StringList,
    firstRoundOps: StringList,
    secondRoundOps: StringList,
    performSingleOp: OperatorFunction
): StringList {
    val simplifiedList: MutableList<String> = mutableListOf()
    var index = 0

    while (index < computeText.size) {
        val element = computeText[index]

        if (element == "(") {
            val openIndex = index
            val closeIndex = getMatchingParenIndex(openIndex, computeText)
            val subText = computeText.subList(openIndex + 1, closeIndex) // cut out start+end parens
            val result = parseText(subText, firstRoundOps, secondRoundOps, performSingleOp, true)
            simplifiedList.add(result.toString())
            index = closeIndex + 1
        } else {
            simplifiedList.add(element)
            index++
        }
    }

    return simplifiedList
}

private fun getMatchingParenIndex(openIndex: Int, computeText: StringList): Int {
    var openCount = 0
    val onlyParens = computeText.withIndex()
        .toList()
        .subList(openIndex, computeText.size)
        .filter { it.value == "(" || it.value == ")" }

    var closeIndex = -1

    for (idxVal in onlyParens) {
        if (idxVal.value == "(") {
            openCount++
        } else if (idxVal.value == ")") {
            openCount--
        }

        if (openCount == 0) {
            closeIndex = idxVal.index
            break
        }
    }

    return closeIndex
}

private fun stripParens(computeText: StringList): StringList {
    return computeText.filter { it != "(" && it != ")" }
}
