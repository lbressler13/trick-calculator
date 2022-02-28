package com.example.trickcalculator.ext

fun <T> List<T>.copyWithReplacement(index: Int, value: T): List<T> {
    val before: List<T> = subList(0, index)
    val after: List<T> = if (index == lastIndex) {
        listOf()
    } else {
        subList(index + 1, size)
    }

    return before + value + after
}

fun <T> List<T>.copyWithLastReplaced(value: T): List<T> {
    return copyWithReplacement(lastIndex, value)
}
