package com.example.trickcalculator.compute

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

    var currentState = computeText

    currentState = parseParens(computeText, firstRoundOps, secondRoundOps, performSingleOp)

    if (firstRoundOps.isNotEmpty()) {
        currentState = parseFirstRound(currentState, firstRoundOps, performSingleOp)
    }

//    val afterFirstRound: StringList = if (firstRoundOps.isEmpty()) {
//        afterParens
//    } else {
//        parseFirstRound(afterParens, firstRoundOps, performSingleOp)
//    }

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
            val result = parseText(subText, firstRoundOps, secondRoundOps, performSingleOp)
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
    // TODO use a fold or while for this
    onlyParens.forEach {
        if (closeIndex == -1) {
            if (it.value == "(") {
                openCount++
            } else if (it.value == ")") {
                openCount--
            }

            if (openCount == 0) {
                closeIndex = it.index
            }
        }
    }

    return closeIndex
}
