package xyz.lbres.trickcalculator.ext.random

import xyz.lbres.trickcalculator.checkDistributedResults
import xyz.lbres.trickcalculator.runTestWithRetry
import kotlin.test.Test
import kotlin.test.assertEquals

class IntRangeRandomExtTest {
    @Test
    fun testSeededRandom() {
        val range = 1..1
        assertEquals(1, range.seededRandom())
        assertEquals(1, range.seededRandom())

        runTestWithRetry {
            checkDistributedResults(1..10, 100000) { (it as IntRange).seededRandom() }
            checkDistributedResults(100..300, 100000) { (it as IntRange).seededRandom() }
        }
    }

    @Test
    fun testSeededShuffled() {
        var range = 3..3
        repeat(10) {
            assertEquals(listOf(3), range.seededShuffled())
        }

        range = 5..7
        val resultOptions = setOf(
            listOf(5, 6, 7),
            listOf(5, 7, 6),
            listOf(6, 5, 7),
            listOf(6, 7, 5),
            listOf(7, 5, 6),
            listOf(7, 6, 5)
        )
        runTestWithRetry {
            checkDistributedResults(resultOptions, 100000) { range.seededShuffled() }
        }
    }
}
