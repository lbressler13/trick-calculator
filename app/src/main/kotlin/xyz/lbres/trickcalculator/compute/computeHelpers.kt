package xyz.lbres.trickcalculator.compute

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.int.ext.isZero
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import java.math.BigInteger

/*
 * Functions that don't perform a main piece of functionality and are used by those that do
 */

/**
 * Determine if a given string is an operator
 *
 * @param element [String]: value to check
 * @param ops [List]: list of operators to check against
 * @return `true` is the element is a member of the ops list, `false` otherwise
 */
fun isOperator(element: String, ops: StringList): Boolean = element in ops

/**
 * Modify the numerator and denominator of an ExactFraction based on a number substitution order.
 * Each digit in the numerator/denominator will be replaced by the corresponding value in the order.
 * For example, 0 will be replaced by the 0th value in the number order.
 * Negative signs are not affected.
 *
 * Assumptions:
 * - Numbers order has passed validation, as specified by [validateNumbersOrder]
 *
 * @param numbersOrder [IntList]: substitution order for digits, can be `null`
 * @param ef [ExactFraction]: number to modify, can be `null`
 * @return [ExactFraction]: number which has been modified as described above, or `null` if [ef] was `null`
 * @throws ArithmeticException divide by zero if the modified denominator has value 0
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
 * Given a list of strings and the index of a left paren, find the index of the corresponding right paren
 *
 * Assumptions:
 * - All validation succeeded, including matched parentheses
 *
 * @param openIndex [Int]: index of opening paren
 * @param computeText [StringList]: list of string values to parse, consisting of operators, numbers, and parens
 * @return [Int] index of closing paren, or -1 if it cannot be found
 */
fun getMatchingParenIndex(openIndex: Int, computeText: StringList): Int {
    var openCount = 0
    val onlyParens = computeText.withIndex()
        .toList()
        .subList(openIndex, computeText.size)
        .filter { it.value == "(" || it.value == ")" }

    for (idxVal in onlyParens) {
        if (idxVal.value == "(") {
            openCount++
        } else if (idxVal.value == ")") {
            openCount--
        }

        if (openCount.isZero()) {
            return idxVal.index
        }
    }

    return -1
}

/**
 * Validate that a number order contains exactly the values 0..9, and that the numbers are not sorted
 *
 * Validations:
 * - Order is not null
 * - Order is a permutation of 0..9
 * - Order is not already sorted
 *
 * @param order [List]: list of numbers, can be `null`
 * @return `true` if validation succeeds, `false` otherwise
 */
fun validateNumbersOrder(order: IntList?): Boolean {
    return order != null &&
        order.joinToString("") != "0123456789" &&
        order.sorted().joinToString("") == "0123456789"
}

/**
 * Generate message to show user after a [NumberFormatException] is thrown
 *
 * @param error [String]: message from initial exception, can be `null`
 * @return [String] new error message with details about the parsing error that occurred
 */
fun getParseErrorMessage(error: String?): String {
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
    } catch (_: Exception) {
        "Cannot parse symbol $symbol"
    }
}
