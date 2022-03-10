package com.example.trickcalculator.compute

import com.example.trickcalculator.bigfraction.BigFraction
import com.example.trickcalculator.bigfraction.BigFractionOverflowException
import com.example.trickcalculator.utils.*
import java.math.BigDecimal

// parse string list and compute mathematical expression, if possible
fun runComputation(
    computeText: StringList,
    firstRoundOps: StringList,
    secondRoundOps: StringList,
    performSingleOp: OperatorFunction,
    numbersOrder: IntList,
    checkParens: Boolean,
    useDecimals: Boolean
): BigFraction {
    if (!validateComputeText(computeText, firstRoundOps + secondRoundOps)) {
        throw Exception("Syntax error")
    }

    var currentState: StringList = computeText

    if (!useDecimals) {
        currentState = stripDecimals(currentState)
    }

    if (validateNumbersOrder(numbersOrder)) {
        currentState = replaceNumbers(currentState, numbersOrder)
    }

    // do this even when not checking parens to add mult operations
    currentState = addMultToParens(currentState)

    if (!checkParens) {
        currentState = stripParens(currentState)
    }

    return try {
        parseText(currentState, firstRoundOps, secondRoundOps, performSingleOp)
    } catch (e: BigFractionOverflowException) {
        if (e.overflowValue != null) {
            throw Exception("Number overflow on value ${e.overflowValue}")
        }

        throw Exception("Number overflow")
    } catch (e: NumberFormatException) {
        val error = getParsingError(e.message)
        throw Exception(error)
    }
}

// map digits to use values in numbers order
fun replaceNumbers(text: StringList, numbersOrder: IntList): StringList {
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

/**
 * Run calculation by parsing text and performing operations
 *
 * Assumptions:
 * - Validation succeeded
 * - Any necessary modifications (i.e. number order, adding x for parens) have already occurred
 */
fun parseText(
    computeText: StringList,
    firstRoundOps: StringList,
    secondRoundOps: StringList,
    performSingleOp: OperatorFunction,
): BigFraction {
    var currentState = computeText

    if (currentState.indexOf("(") != -1) {
        currentState = parseParens(computeText, firstRoundOps, secondRoundOps, performSingleOp)
    }

    if (firstRoundOps.isNotEmpty()) {
        currentState = parseSetOfOps(currentState, firstRoundOps, performSingleOp)
    }

    if (secondRoundOps.isNotEmpty()) {
        currentState = parseSetOfOps(currentState, secondRoundOps, performSingleOp)
    }

    return when (currentState.size) {
        0 -> BigFraction.ZERO
        1 -> BigFraction(currentState[0])
        else -> throw Exception("Parse error")
    }
}

fun parseSetOfOps(
    computeText: StringList,
    ops: StringList,
    performSingleOp: OperatorFunction
): StringList {
    val simplifiedList: MutableList<String> = mutableListOf()

    var index = 0

    while (index < computeText.size) {
        val element = computeText[index]

        if (element !in ops) {
            simplifiedList.add(element)
            index++
        } else {
            // don't have to worry about out of bounds or parse errors b/c of validation
            val leftVal = BigFraction(simplifiedList.last())
            val rightVal = BigFraction(computeText[index + 1])
            val result = performSingleOp(leftVal, rightVal, element)
            val lastIndex = simplifiedList.lastIndex

            simplifiedList[lastIndex] = result.toBFString()

            // skip past next value, which was already used as rightValue
            index += 2
        }
    }

    return simplifiedList
}

/**
 * Simplify each set of parentheses to a single value by recursive calls to parseText.
 * Should only be called if checkParens = true
 *
 * Assumptions:
 * - Validation succeeded, including matched parentheses
 * - Any necessary modifications have already occurred, including add multiplication around parens as needed
 */
fun parseParens(
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
            val result = parseText(subText, firstRoundOps, secondRoundOps, performSingleOp)
            simplifiedList.add(result.toBFString())
            index = closeIndex + 1
        } else {
            simplifiedList.add(element)
            index++
        }
    }

    return simplifiedList
}

// add times operator symbol when numbers are directly next to parens
fun addMultToParens(computeText: StringList): StringList {
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
            (lastType == "rparen" && currentType == "lparen")
        ) {
            augmentedList.add("x")
            augmentedList.add(it)
        } else {
            augmentedList.add(it)
        }

        lastType = currentType
    }

    return augmentedList
}

fun getMatchingParenIndex(openIndex: Int, computeText: StringList): Int {
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

fun stripParens(computeText: StringList): StringList = computeText.filter { it != "(" && it != ")" }

fun stripDecimals(computeText: StringList): StringList = computeText.map { element ->
    element.filter { it != '.' }
}

fun getParsingError(error: String?): String {
    if (error == null) {
        return "Parse error"
    }

    val startIndex = error.indexOf("\"")
    val endIndex = error.lastIndexOf("\"")

    if (endIndex - startIndex <= 1) {
        return "Parse error"
    }

    val symbol = error.substring(startIndex + 1, endIndex)

    return try {
        BigDecimal(symbol)
        "Number overflow on value $symbol"
    } catch (e: Exception) {
        "Cannot parse symbol $symbol"
    }
}
