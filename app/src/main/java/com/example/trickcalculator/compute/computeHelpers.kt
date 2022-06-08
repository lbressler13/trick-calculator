package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.IntList
import com.example.trickcalculator.utils.StringList
import exactfraction.ExactFraction
import java.math.BigInteger

/**
 * Determine if a given string is an operator
 *
 * @param element [String]: value to check
 * @param ops [List]: list of operators to check against
 * @return true is the element is a member of the ops list, false otherwise
 */
fun isOperator(element: String, ops: StringList): Boolean = element in ops

// TODO write tests for this
fun applyOrderToEF(numbersOrder: IntList?, ef: ExactFraction?): ExactFraction? {
    if (numbersOrder == null || ef == null) {
        return ef
    }

    val numString = ef.numerator.toString()
    val denomString = ef.denominator.toString()

    val newNumString = numString.map {
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
