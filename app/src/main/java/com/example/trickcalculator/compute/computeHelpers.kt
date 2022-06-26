package com.example.trickcalculator.compute

import kotlinutils.list.IntList
import kotlinutils.list.StringList
import exactfraction.ExactFraction
import java.math.BigInteger

/**
 * Functions that don't perform a main piece of functionality and are used by those that do
 */

/**
 * Determine if a given string is an operator
 *
 * @param element [String]: value to check
 * @param ops [List]: list of operators to check against
 * @return true is the element is a member of the ops list, false otherwise
 */
fun isOperator(element: String, ops: StringList): Boolean = element in ops

/**
 * Modify the numerator and denominator of an ExactFraction based on a number substitution order.
 * Each digit in the numerator/denominator will be replaced by the corresponding value in the order.
 * For example, 0 is replaced by the 0th value in the number order.
 * Negative signs are not affected.
 *
 * Assumptions:
 * - If numbers order is not null, it has size of 10 and contains digits 0 through 9, in some order
 *
 * @param numbersOrder [IntList]: substitution order for digits, possibly null
 * @param ef [ExactFraction]: number to modify, possibly null
 * @return [ExactFraction]: number which has been modified as described above, or null if the ef param was null
 * @throws ArithmeticException: divide by zero if the modified denominator has value 0
 */
fun applyOrderToEF(numbersOrder: IntList?, ef: ExactFraction?): ExactFraction? {
    if (numbersOrder == null || ef == null) {
        return ef
    }

    val numString = ef.numerator.toString()
    val denomString = ef.denominator.toString()

    val newNumString = numString.map {
        // keep negative sign in numerator
        if (it == '-') {
            '-'
        } else {
            val index = it.digitToInt()
            numbersOrder[index].toString()
        }
    }.joinToString("")

    val newDenomString = denomString.map {
        val index = it.digitToInt()
        numbersOrder[index].toString()
    }.joinToString("")

    val newNum = BigInteger(newNumString)
    val newDenom = BigInteger(newDenomString)
    return ExactFraction(newNum, newDenom)
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
