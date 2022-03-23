package com.example.trickcalculator.ext

import com.example.trickcalculator.exactdecimal.Expression
import com.example.trickcalculator.exactdecimal.Term

/**
 * Create a copy of a list, with one value changed
 *
 * @param index [Int]: index of value to change
 * @param value [T]: value to substitute at index
 * @return list identical to this, with the exception of the value at index i
 * @throws IndexOutOfBoundsException if index is less than zero or greater than lastIndex
 */
fun <T> List<T>.copyWithReplacement(index: Int, value: T): List<T> {
    val before: List<T> = subList(0, index)
    val after: List<T> = if (index == lastIndex) {
        listOf()
    } else {
        subList(index + 1, size)
    }

    return before + value + after
}

/**
 * Create a copy of a list, with the last value changed
 *
 * @param value [T]: new value for last index
 * @return list identical to this, with the exception of the value at the last index
 */
fun <T> List<T>.copyWithLastReplaced(value: T): List<T> = copyWithReplacement(lastIndex, value)

fun <T> List<T>.subListTo(index: Int): List<T> = subList(0, index)
fun <T> List<T>.subListFrom(index: Int): List<T> = subList(index, size)

fun <T> List<T>.dropFirst(checkFn: (T) -> Boolean): List<T> {
    val firstIndex = indexOfFirst(checkFn)
    if (firstIndex == -1) {
        throw NoSuchElementException()
    }

    val start = if (firstIndex == 0) listOf() else subListTo(firstIndex)
    val end = if (firstIndex == lastIndex) listOf() else subListFrom(firstIndex + 1)
    return start + end
}

/**
 * Create Expression from list of Terms
 *
 * @return expression using this list of terms
 */
fun List<Term>.asExpression(): Expression = Expression(this)

/**
 * Sort function for TermList
 *
 * @return terms sorted by exponent
 */
fun List<Term>.sorted(): List<Term> = sortedBy { it.exp }

/**
 * Simplify a list of expressions to a single expression via multiplication
 *
 * @return single expression, which is the product of all expressions in list
 */
fun List<Expression>.toExpression(): Expression =
    fold(Expression()) { acc, current -> acc * current }
