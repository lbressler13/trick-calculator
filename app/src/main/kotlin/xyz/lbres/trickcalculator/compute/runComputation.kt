package xyz.lbres.trickcalculator.compute

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.exactfraction.ExactFractionOverflowException
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.utils.AppLogger
import xyz.lbres.trickcalculator.utils.OperatorFunction

/**
 * Parse string list and compute as a mathematical expression, if possible.
 * Includes list validation, modifying the list based on parameters, and running the computation itself.
 *
 * @param initialValue [ExactFraction]: the previously computed value, if being used as the start of the computation
 * @param initialText [StringList]: list of string values to parse, consisting of operators, numbers, and parens
 * @param operatorRounds [List<StringList>]: list of string values recognized as operators, separated into "rounds" that should be applied in the same passthrough
 * @param performSingleOp [OperatorFunction]: given an operator and 2 numbers, applies the operator to the numbers
 * @param numbersOrder [List]: list of numbers, which can be used to reassign the values of digits
 * @param applyParens [Boolean]: if parentheses should be recognized.
 * If false, all parentheses will be removed before computation.
 * No numbers or operators will be affected.
 * @param applyDecimals [Boolean]: if decimal points should be recognized.
 * If false, the decimal point will be removed from each number and numbers will be processed using only the digits.
 * @param shuffleComputation [Boolean]: if order of numbers and order of ops should be shuffled
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
    applyDecimals: Boolean,
    shuffleComputation: Boolean
): ExactFraction {
    val validatedNumOrder = ternaryIf(validateNumbersOrder(numbersOrder), numbersOrder, null)
    val modifiedInitialValue = applyOrderToEF(validatedNumOrder, initialValue)

    val computeText = generateAndValidateComputeText(
        modifiedInitialValue,
        initialText,
        operatorRounds.flatten(),
        validatedNumOrder,
        applyParens,
        applyDecimals,
        shuffleComputation
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
 * @param operatorRounds [List]: list of lists, where each sublist contains a round of string operators.
 * Each sublist is applied as a round of operators, starting with the first sublist and ending with the last.
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

    if (currentState.contains("(")) {
        currentState = parseParens(computeText, operatorRounds, performSingleOp)
    }

    for (round in operatorRounds) {
        if (round.isNotEmpty()) {
            currentState = parseOperatorRound(currentState, round, performSingleOp)
        }
    }

    return when (currentState.size) {
        0 -> ExactFraction.ZERO
        1 -> ExactFraction(currentState[0])
        else -> {
            AppLogger.e(null, "Failed to compute single result for $computeText. Result: $currentState")
            throw Exception("Parse error")
        }
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
fun parseOperatorRound(
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
 * @param operatorRounds [List<StringList>]: list of string values recognized as operators, separated into "rounds" that should be applied in the same passthrough.
 * Passed to parseText function when called.
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
