package xyz.lbres.trickcalculator.ext.random

import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.checkDistributedResults
import xyz.lbres.trickcalculator.runTestWithRetry
import kotlin.test.Test
import kotlin.test.assertEquals

class ListRandomExtTest {
    @Test
    fun testSeededRandom() {
        var intList = listOf(1)
        assertEquals(1, intList.seededRandom())
        assertEquals(1, intList.seededRandom())

        intList = listOf(1, 1, 1, 1)
        assertEquals(1, intList.seededRandom())
        assertEquals(1, intList.seededRandom())

        runTestWithRetry {
            checkDistributedResults(List(10) { it }, 1000) { (it as IntList).seededRandom() }
            checkDistributedResults(List(10) { it * 2 }, 1000) { (it as IntList).seededRandom() }

            val stringList = listOf("hello", "world", "hello world", "goodbye", "planet", "farewell", "earth")
            checkDistributedResults(stringList, 100000) { (it as StringList).seededRandom() }

            val listList = listOf(emptyList(), listOf("hello"), listOf("12345"), listOf("", "1", "11", "111"), listOf("goodbye", "world"))
            checkDistributedResults(listList, 1000) { (it as List<StringList>).seededRandom() }
        }
    }

    @Test
    fun testSeededShuffled() {
        val intList = listOf(7, 5, 6)
        val stringList = listOf("6", "7", "5")

        val intResultOptions = setOf(
            listOf(5, 6, 7),
            listOf(5, 7, 6),
            listOf(6, 5, 7),
            listOf(6, 7, 5),
            listOf(7, 5, 6),
            listOf(7, 6, 5),
        )
        val stringResultOptions = intResultOptions.map { it.map(Int::toString) }

        runTestWithRetry {
            checkDistributedResults(intResultOptions, 100000) { intList.seededShuffled() }
            checkDistributedResults(stringResultOptions, 100000) { stringList.seededShuffled() }
        }
    }
}
