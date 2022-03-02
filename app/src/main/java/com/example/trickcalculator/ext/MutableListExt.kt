package com.example.trickcalculator.ext

fun <T> MutableList<T>.replaceAt(index: Int, newItem: T) {
    removeAt(index)
    add(index, newItem)
}

fun <T> MutableList<T>.replaceLast(newItem: T) {
    replaceAt(lastIndex, newItem)
}

fun <T> MutableList<T>.pop(): T? {
    if (isNullOrEmpty()) {
        return null
    }

    val last: T = last()
    removeLast()
    return last
}
