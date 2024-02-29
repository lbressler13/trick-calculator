package xyz.lbres.trickcalculator.ui.history

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
    val shuffledHistory = history.seededShuffled()
        .zip(history.seededShuffled()) { item1: HistoryItem, item2: HistoryItem ->
            if (item2.result != null) {
                HistoryItem(item1.computation, item2.result)
            } else {
                HistoryItem(item1.computation, item2.error!!)
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
    return when {
        history.isEmpty() -> null
        random.nextBoolean(0.2f) -> null
        else -> List(history.size) { generateRandomHistoryItem() }
    }
}
