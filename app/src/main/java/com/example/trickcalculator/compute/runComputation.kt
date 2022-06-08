package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.*
import exactfraction.ExactFraction
import exactfraction.ExactFractionOverflowException

/**
 * Parse string list and compute as a mathematical expression, if possible.
 * Includes list validation, modifying the list based on parameters, and running the computation itself.
 *
 * @param computeText [List]: list of string values to parse, consisting of operators, numbers, and parens
 * @param firstRoundOps [List]: list of string operators to be applied in the first round of computation.
 * Likely multiplication and division
 * @param secondRoundOps [List]: list of string operators to be applied in the second round of computation.
 * Likely addition and subtraction
 * @param performSingleOp [OperatorFunction]: given an operator and 2 numbers, applies the operator to the numbers
 * @param numbersOrder [List]: list of numbers, which can be used to reassign the values of digits
 * @param applyParens [Boolean]: if parentheses should be recognized.
 * If false, all parentheses will be removed before computation.
 * No numbers or operators will be affected.
 * @param applyDecimals [Boolean]: if decimal points should be recognized.
 * If false, the decimal point will be removed from each number and numbers will be processed using only the digits.
 * @return ExactFraction containing the single computed value
 * @throws ArithmeticException in case of divide by zero
 * @throws Exception in case of issues with syntax, parsing, or number overflow
 */
fun runComputation(
    initialValue: ExactFraction?,
    initialText: StringList,
    operatorRounds: List<StringList>,
    performSingleOp: OperatorFunction,
    numbersOrder: IntList,
    applyParens: Boolean,
    applyDecimals: Boolean
): ExactFraction {
    val validatedNumOrder = if (validateNumbersOrder(numbersOrder)) {
        numbersOrder
    } else {
        null
    }

    val modifiedInitialValue = applyOrderToEF(validatedNumOrder, initialValue)

    val computeText = buildAndValidateComputeText(
        modifiedInitialValue,
        initialText,
        operatorRounds.flatten(),
        validatedNumOrder,
        applyParens,
        applyDecimals
    )

    return try {
        parseText(computeText, operatorRounds, performSingleOp)
    } catch (e: ExactFractionOverflowException) {
        if (e.overflowValue != null) {
            throw Exception("Number overflow on value ${e.overflowValue}")
        }

        throw Exception("Number overflow")
    } catch (e: NumberFormatException) {
        val error = getParsingError(e.message)
        throw Exception(error)
    }
}

/**
 * Run calculation by parsing text and performing operations
 *
 * Assumptions:
 * - Validation succeeded
 * - Any necessary modifications (i.e. number order, adding x for parens) have already occurred
 *
 * @param computeText [List]: list of string values to parse, consisting of operators, numbers, and parens
 * @param firstRoundOps [List]: list of string operators to be applied in the first round of computation.
 * Likely multiplication and division
 * @param secondRoundOps [List]: list of string operators to be applied in the second round of computation.
 * Likely addition and subtraction
 * @param performSingleOp [OperatorFunction]: given an operator and 2 numbers, applies the operator to the numbers
 * @return ExactFraction containing the single computed value
 * @throws ArithmeticException in case of divide by zero
 * @throws Exception in case of issues with parsing
 */
fun parseText(
    computeText: StringList,
    operatorRounds: List<StringList>,
    performSingleOp: OperatorFunction,
): ExactFraction {
    var currentState = computeText

    if (currentState.indexOf("(") != -1) {
        currentState = parseParens(computeText, operatorRounds, performSingleOp)
    }

    for (round in operatorRounds) {
        if (round.isNotEmpty()) {
            currentState = parseSetOfOps(currentState, round, performSingleOp)
        }
    }

    return when (currentState.size) {
        0 -> ExactFraction.ZERO
        1 -> ExactFraction(currentState[0])
        else -> throw Exception("Parse error")
    }
}

/**
 * Computes a single set of operators over the text, ignoring all other operators and all numbers not affected by the given operators
 *
 * Assumptions:
 * - Validation succeeded
 * - Compute text contains no parentheses
 * - Any necessary modifications (i.e. number order, adding x for parens) have already occurred
 *
 * @param computeText [List]: list of string values to parse, consisting of operators and numbers
 * @param ops [List]: list of string operators to be applied
 * @param performSingleOp [OperatorFunction]: given an operator and 2 numbers, applies the operator to the numbers
 * @return modified list where each application of a given operator has been reduced to a single ExactFraction, represented as a EF string
 * @throws ArithmeticException in case of divide by zero
 * @throws Exception in case of issues with parsing
 */
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
            val leftVal = ExactFraction(simplifiedList.last())
            val rightVal = ExactFraction(computeText[index + 1])
            val result = performSingleOp(leftVal, rightVal, element)
            val lastIndex = simplifiedList.lastIndex

            simplifiedList[lastIndex] = result.toEFString()

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
 *
 * @param computeText [List]: list of string values to parse, consisting of operators, numbers, and parens
 * @param firstRoundOps [List]: list of string operators to be applied in the first round of computation.
 * Likely multiplication and division
 * @param secondRoundOps [List]: list of string operators to be applied in the second round of computation.
 * Likely addition and subtraction
 * @param performSingleOp [OperatorFunction]: given an operator and 2 numbers, applies the operator to the numbers
 * @return a modified string list where all pairs of parentheses have been simplified to a single ExactFraction, represented as a EF string
 */
fun parseParens(
    computeText: StringList,
    operatorRounds: List<StringList>,
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
            val result = parseText(subText, operatorRounds, performSingleOp)
            simplifiedList.add(result.toEFString())
            index = closeIndex + 1
        } else {
            simplifiedList.add(element)
            index++
        }
    }

    return simplifiedList
}

/**
 * Given a list of text and the index of a left paren, find the index of the corresponding right paren
 *
 * Assumptions:
 * - Validation succeeded, including matched parentheses
 *
 * @param openIndex [Int]: index of opening paren
 * @param computeText [List]: list of string values to parse, consisting of operators, numbers, and parens
 * @return index of closing paren, or -1 if it cannot be found
 * @throws IndexOutOfBoundsException if openIndex is greater than lastIndex
 */
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

/**
 * Generate a specific error message after a NumberFormatException
 *
 * @param error [String]: message from initial exception, can be null
 * @return new error message with details about the parsing error that occurred
 */
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
        ExactFraction(symbol)
        "Number overflow on value $symbol"
    } catch (e: Exception) {
        "Cannot parse symbol $symbol"
    }
}
