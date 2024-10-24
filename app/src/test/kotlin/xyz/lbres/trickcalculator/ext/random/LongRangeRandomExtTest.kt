package xyz.lbres.trickcalculator.ext.random

import xyz.lbres.trickcalculator.checkDistributedResults
import xyz.lbres.trickcalculator.runTestWithRetry
import kotlin.test.Test
import kotlin.test.assertEquals

class LongRangeRandomExtTest {
    @Test
    fun testSeededRandom() {
        val range = 1L..1L
        assertEquals(1L, range.seededRandom())
        assertEquals(1L, range.seededRandom())

        runTestWithRetry {
            checkDistributedResults(1L..10L, 100000) { (it as LongRange).seededRandom() }
            checkDistributedResults(100L..300L, 100000) { (it as LongRange).seededRandom() }
        }
    }
}
