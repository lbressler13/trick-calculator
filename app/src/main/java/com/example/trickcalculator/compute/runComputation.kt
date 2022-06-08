package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.*
import exactfraction.ExactFraction
import exactfraction.ExactFractionOverflowException
import java.math.BigInteger

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

    val finalText = buildAndValidateComputeText(
        modifiedInitialValue,
        initialText,
        operatorRounds.flatten(),
        validatedNumOrder,
        applyParens,
        applyDecimals
    )

    return try {
        parseText(finalText, operatorRounds, performSingleOp)
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
 * Add times operator symbol when numbers are directly next to parens.
 * Adds the string "x", even if this operator does not correspond to multiplication.
 * All numbers, parens, and other operators are not affected
 *
 * Assumptions:
 * - Validation succeeded, including matched parentheses
 *
 * @param computeText [List]: list of string values to parse, consisting of operators, numbers, and parens
 * @return a modified string list where no number or set of parentheses is directly adjacent to a set of parentheses
 */
//fun addMultToParens(firstValue: ExactFraction?, computeText: StringList): StringList {
//    val augmentedList: MutableList<String> = mutableListOf()
//
//    var lastType = ""
//
//    val getType: (String) -> String = {
//        when {
//            it[0] == '.' || it[0].isDigit() -> "number"
//            it == "(" -> "lparen"
//            it == ")" -> "rparen"
//            else -> ""
//        }
//    }
//
//    computeText.forEach {
//        val currentType = getType(it)
//
//        // check for number next to closed set of parens, or adjacent sets of parens
//        if ((lastType == "number" && currentType == "lparen") ||
//            (lastType == "rparen" && currentType == "number") ||
//            (lastType == "rparen" && currentType == "lparen")
//        ) {
//            augmentedList.add("x")
//            augmentedList.add(it)
//        } else {
//            augmentedList.add(it)
//        }
//
//        lastType = currentType
//    }
//
//    if (firstValue != null && augmentedList.isNotEmpty() && getType(augmentedList[0]) != "") {
//        augmentedList.add(0, "x")
//    }
//
//    return augmentedList
//}

/**
 * Update digits using values in number order, and in the numerator/denominator of an ExactFraction.
 * Does not affect any symbols other in the string.
 * Single-digit numbers are replaced by the value of the corresponding index in the number order.
 * For example, 0 is replaced by the 0th value in the number order.
 * Multi-digit numbers are not modified, as these will cause later validation to fail
 *
 * Assumptions:
 * - Numbers order has passed validation
 *
 * @param ef [ExactFraction]
 * @param text [List]: string list of numbers, operators, and parens
 * @param numbersOrder [List]: list of numbers, containing the values 0..9 in any other order
 * @return [Pair] an ExactFraction where the digits in the numerator and denominator have been modified,
 * and a list which is identical to the initial text, in everything other than the values of numbers.
 * Values of numbers have been modified as described above.
 */
//fun replaceNumbers(
//    ef: ExactFraction?,
//    text: StringList,
//    numbersOrder: IntList
//): Pair<StringList, ExactFraction?> {
//    val newText = text.map {
//        if (it.length > 1 || !it[0].isDigit()) {
//            it
//        } else {
//            val index = Integer.parseInt(it)
//            numbersOrder[index].toString()
//        }
//    }
//
//    if (ef == null) {
//        return Pair(newText, null)
//    }
//
//    // modify digits in numerator
//    val numerator = ef.numerator.toString()
//    val newNumString = numerator.map {
//        if (it == '-') { // handle negative sign
//            "-"
//        } else {
//            val index = Integer.parseInt(it.toString())
//            numbersOrder[index].toString()
//        }
//    }.joinToString("")
//
//    // modify digits in denominator
//    val denominator = ef.denominator.toString()
//    val newDenomString = denominator.map {
//        val index = Integer.parseInt(it.toString())
//        numbersOrder[index].toString()
//    }.joinToString("")
//
//    val newEF = ExactFraction(BigInteger(newNumString), BigInteger(newDenomString))
//    return Pair(newText, newEF)
//}

/**
 * Given a list of text, remove all open and close parens.
 * No operators or numbers are affected.
 *
 * Assumptions:
 * - Validation succeeded
 * - Every set of parentheses is separated from numbers and other parentheses, using an operator
 *
 * @param computeText [List]: list of string values to parse, consisting of operators, numbers, and parens
 * @return modified list containing all numbers and operators, without any parens
 */
// fun stripParens(computeText: StringList): StringList = computeText.filter { it != "(" && it != ")" }

/**
 * Given a list of text, remove decimals points from all numbers.
 * No operators, parens, or non-decimal numbers are affected.
 * In addition, no digits are deleted.
 * For example, the number 2.0 with become 20, not 2
 *
 * Assumptions:
 * - Validation succeeded
 *
 * @param computeText [List]: list of string values to parse, consisting of operators, numbers, and parens
 * @return modified list containing all operators and parens, with numbers modified to remove any decimal points
 */
// fun stripDecimals(computeText: StringList): StringList = computeText.filter { it != "." }

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
