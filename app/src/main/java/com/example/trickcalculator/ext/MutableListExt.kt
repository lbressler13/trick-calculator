package com.example.trickcalculator.ext

fun <T> MutableList<T>.popRandom(): T? {
    if (isEmpty()) {
        return null
    }

    val index = indices.random()
    val element = get(index)
    removeAt(index)
    return element
}
