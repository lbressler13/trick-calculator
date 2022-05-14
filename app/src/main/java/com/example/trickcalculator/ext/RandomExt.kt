package com.example.trickcalculator.ext

import kotlin.random.Random

fun Random.nextBoolean(probabilityTrue: Float): Boolean {
    return nextFloat() < probabilityTrue
}

fun <T> Random.nextFromWeightedList(weightedPairs: List<Pair<T, Float>>): T {
    val f = nextFloat()
    var runningTotal = 0f

    for (pair in weightedPairs) {
        runningTotal += pair.second
        if (f < pair.second) {
            return pair.first
        }
    }

    return weightedPairs.last().first
}
