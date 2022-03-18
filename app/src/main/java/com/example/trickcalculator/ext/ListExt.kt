package com.example.trickcalculator.ext

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

fun <T> List<T>.isSingleItem(): Boolean = size == 1
