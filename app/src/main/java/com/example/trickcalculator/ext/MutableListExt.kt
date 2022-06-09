package com.example.trickcalculator.ext

/**
 * Remove a random element from list and return it
 *
 * @return [T]: an element from the list, or null if the list is empty
 */
fun <T> MutableList<T>.popRandom(): T? {
    if (isEmpty()) {
        return null
    }

    val index = indices.random()
    val element = get(index)
    removeAt(index)
    return element
}
