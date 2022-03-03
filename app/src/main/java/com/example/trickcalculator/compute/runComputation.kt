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
): Float {
    if (!validateComputeText(computeText, firstRoundOps + secondRoundOps)) {
        throw Exception("Syntax error")
    }

    var currentState: StringList = computeText

    if (validateNumbersOrder(numbersOrder)) {
        currentState = replaceNumbers(currentState, numbersOrder)
    }

    // do this even when not checking parens to add mult operations
    currentState = addMultToParens(currentState)

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
        if (!isNumber(it)) {
            it
        } else {
            it.map { c ->
                if (c.isDigit()) {
                    val index = Integer.parseInt(c.toString())
                    numbersOrder[index].toString()
                } else {
                    c.toString()
                }
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
): Float {
    var total: Float = 0f
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
            currentOperator == null -> total = element.toFloat()
            else -> {
                val currentVal: Float = element.toFloat()
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
            val leftVal: Float = simplifiedList.last().toFloat()
            val rightVal: Float = computeText[index + 1].toFloat()
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

// add times operator symbol when numbers are directly next to parens
private fun addMultToParens(computeText: StringList): StringList {
    val augmentedList: MutableList<String> = mutableListOf()

    var lastType = ""

    computeText.forEach {
        val currentType: String = when {
            isNumber(it) -> "number"
            it == "(" -> "lparen"
            it == ")" -> "rparen"
            else -> ""
        }

        // check for number next to closed set of parens, or adjacent sets of parens
        if ((lastType == "number" && currentType == "lparen") ||
            (lastType == "rparen" && currentType == "number") ||
            (lastType == "rparen" && currentType == "lparen")) {
            augmentedList.add("x")
            augmentedList.add(it)
        } else {
            augmentedList.add(it)
        }

        lastType = currentType
    }

    return augmentedList
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
