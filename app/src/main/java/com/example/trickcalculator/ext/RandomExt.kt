package com.example.trickcalculator.ext

import kotlin.random.Random

/**
 * Get next boolean with a probability other than 0.5
 *
 * @param probabilityTrue [Float]: probability of returning true, must be between 0 and 1
 * @return [Boolean]
 * @throws [IllegalArgumentException] if probability is not between 0 and 1
 */
fun Random.nextBoolean(probabilityTrue: Float): Boolean {
    if (probabilityTrue !in (0f..1f)) {
        throw IllegalArgumentException("Probability must be in range 0f..1f")
    }
    return nextFloat() < probabilityTrue
}

/**
 * Get a random value from a weighted list, based on the weights
 *
 * @param weightedPairs [List]: list of pairs, which consist of an item and a weight.
 * Weights must be non-negative, and sum of weights must equal 1.
 * @return [T]: the randomly selected value
 * @throws [IllegalArgumentException] if a weight is less than zero, or sum of weights is not 1
 */
fun <T> Random.nextFromWeightedList(weightedPairs: List<Pair<T, Float>>): T {
    // check for invalid weights
    if (weightedPairs.any { it.second < 0f }) {
        throw IllegalArgumentException("Weights cannot be less than zero")
    }

    val totalWeights = weightedPairs.fold(0f) { acc, pair -> acc + pair.second }
    if (totalWeights != 1f) {
        throw IllegalArgumentException("Weights must total 1")
    }

    // select value
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
