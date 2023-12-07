package xyz.lbres.trickcalculator.ui.history

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.general.simpleIf
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.random.ext.nextBoolean
import xyz.lbres.trickcalculator.SharedValues.random
import xyz.lbres.trickcalculator.utils.History
import xyz.lbres.trickcalculator.utils.seededShuffled

fun generateRandomHistory(history: History, randomness: Int?): History {
    return when (randomness) {
        0 -> history // no randomness
        1 -> history.seededShuffled() // shuffled order
        2 -> shuffleHistoryValues(history) // shuffled values
        3 -> createRandomizedHistory(history) ?: emptyList() // random generation
        else -> history
    }
}

/**
 * Shuffle history computations and results/errors.
 * Returns a list that contains all computations and results/errors, but not necessarily in their original pari.
 *
 * @return [History]: history where computations and values have been shuffled
 */
private fun shuffleHistoryValues(history: History): History {
    val computations: List<StringList> = history.map { it.computation }.seededShuffled()
    val values: List<Pair<ExactFraction?, String?>> = history.map { Pair(it.result, it.error) }.seededShuffled()

    val shuffledHistory = computations.mapIndexed { index, comp ->
        val valuePair = values[index]
        if (valuePair.first != null) {
            HistoryItem(comp, valuePair.first!!)
        } else {
            HistoryItem(comp, valuePair.second!!)
        }
    }

    return shuffledHistory
}

/**
 * Generate randomized history items based on the length of the input.
 * Returns null randomly or if history is empty, to indicate an "error" in retrieving history.
 *
 * @return [History]: possibly null history of generated computations, with same length as real history (if not null)
 */
private fun createRandomizedHistory(history: History): History? {
    val probabilityError = simpleIf(history.isEmpty(), 1f, 0.2f)

    if (random.nextBoolean(probabilityError)) {
        return null
    }

    return List(history.size) { generateRandomHistoryItem() }
}
