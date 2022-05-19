package com.example.trickcalculator.ext

import kotlin.random.Random

fun Random.nextBoolean(probabilityTrue: Float): Boolean {
    if (probabilityTrue !in (0f..1f)) {
        throw IllegalArgumentException("Probability must be in range 0f..1f")
    }
    return nextFloat() < probabilityTrue
}

fun <T> Random.nextFromWeightedList(weightedPairs: List<Pair<T, Float>>): T {
    if (weightedPairs.any { it.second < 0f }) {
        throw IllegalArgumentException("Weights cannot be less than zero")
    }

    val totalWeights = weightedPairs.fold(0f) { acc, pair -> acc + pair.second }
    if (totalWeights != 1f) {
        throw IllegalArgumentException("Weights must total 1")
    }
    val f = nextFloat()
    var runningTotal = 0f

    for (pair in weightedPairs) {
        runningTotal += pair.second
        if (f < runningTotal) {
            return pair.first
        }
    }

    return weightedPairs.last().first
}
