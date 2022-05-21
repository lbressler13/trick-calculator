package com.example.trickcalculator.ext

import org.junit.Test
import org.junit.Assert.*
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class RandomExtTest {
    private val random = Random.Default

    @Test
    fun testNextBoolean() {
        runSingleNextBooleanTest(0f)
        runSingleNextBooleanTest(1f)

        runSingleNextBooleanTest(0.1f)
        runSingleNextBooleanTest(0.25f)
        runSingleNextBooleanTest(0.5f)
        runSingleNextBooleanTest(0.9f)

        val expectedMessage = "Probability must be in range 0f..1f"
        var error = assertThrows(IllegalArgumentException::class.java) {
            random.nextBoolean(-0.5f)
        }
        assertEquals(expectedMessage, error.message)

        error = assertThrows(IllegalArgumentException::class.java) {
            random.nextBoolean(1.5f)
        }
        assertEquals(expectedMessage, error.message)
    }

    private fun runSingleNextBooleanTest(probability: Float) {
        val iterations = 1000
        val results = List(iterations) { random.nextBoolean(probability) }
            .groupBy { it }
            .map { it.key to it.value.size }

        val falsePair = results.find { !it.first } ?: false to 0
        val truePair = results.find { it.first } ?: true to 0

        when (probability) {
            0f -> {
                assertEquals(0, truePair.second)
                assertEquals(iterations, falsePair.second)
            }
            1f -> {
                assertEquals(iterations, truePair.second)
                assertEquals(0, falsePair.second)
            }
            else -> {
                val errorRange = 0.05f
                val minTrueFloat = max(probability - errorRange, 0f)
                val maxTrueFloat = min(probability + errorRange, 1f)
                val minTrue = (minTrueFloat * iterations).toInt()
                val maxTrue = (maxTrueFloat * iterations).toInt()

                assertEquals(iterations, truePair.second + falsePair.second)
                assertTrue(truePair.second in (minTrue..maxTrue))
            }
        }
    }

    @Test
    fun testNextFromWeightedList() {
        // errors
        val expectedError = "Weights must total 1"
        var error = assertThrows(java.lang.IllegalArgumentException::class.java) {
            random.nextFromWeightedList(listOf())
        }
        assertEquals(expectedError, error.message)

        var list = listOf(
            Pair(1, 0.1f),
            Pair(2, 0.2f),
            Pair(3, 0.75f)
        )
        error = assertThrows(java.lang.IllegalArgumentException::class.java) {
            random.nextFromWeightedList(list)
        }
        assertEquals(expectedError, error.message)

        list = listOf(
            Pair(1, 0.1f),
            Pair(2, 0.2f),
            Pair(3, 0.6f)
        )
        error = assertThrows(java.lang.IllegalArgumentException::class.java) {
            random.nextFromWeightedList(list)
        }
        assertEquals(expectedError, error.message)

        list = listOf(
            Pair(1, 0f),
            Pair(2, 0f),
        )
        error = assertThrows(java.lang.IllegalArgumentException::class.java) {
            random.nextFromWeightedList(list)
        }
        assertEquals(expectedError, error.message)

        list = listOf(
            Pair(1, -0.1f),
            Pair(2, 0.5f),
            Pair(6, 0.6f),
        )
        error = assertThrows(java.lang.IllegalArgumentException::class.java) {
            random.nextFromWeightedList(list)
        }
        assertEquals("Weights cannot be less than zero", error.message)

        // tests with int
        list = listOf(Pair(3, 1f))
        runSingleNextFromWeightedListTest(list)

        list = listOf(Pair(3, 1f), Pair(2, 0f))
        runSingleNextFromWeightedListTest(list)

        list = listOf(Pair(3, 0.5f), Pair(4, 0.5f))
        runSingleNextFromWeightedListTest(list)

        list = listOf(Pair(3, 0.25f), Pair(4, 0.75f))
        runSingleNextFromWeightedListTest(list)

        list = listOf(Pair(-1, 0.15f), Pair(0, 0.3f), Pair(1, 0.55f))
        runSingleNextFromWeightedListTest(list)

        list = listOf(Pair(0, 0.1f), Pair(-1, 0.2f), Pair(5, 0.45f), Pair(2, 0.25f))//, Pair(13, 0.1f))
        runSingleNextFromWeightedListTest(list)

        // other types
        val stringList = listOf(Pair("hello", 0.3f), Pair("world", 0.65f), Pair("goodbye", 0.05f))
        runSingleNextFromWeightedListTest(stringList)

        val rangeList = listOf(Pair(0..1000, 0.2f), Pair(1000..10000, 0.6f), Pair(10000..1000000, 0.2f))
        runSingleNextFromWeightedListTest(rangeList)
    }

    private fun <T> runSingleNextFromWeightedListTest(list: List<Pair<T, Float>>) {
        val iterations = 10000
        val results = List(iterations) { random.nextFromWeightedList(list) }
            .groupBy { it }
            .map { it.key to it.value.size }


        val totalResults = results.sumOf { it.second }
        assertEquals(iterations, totalResults)

        for (item in list) {
            val weight = item.second
            val result: Pair<T, Int> =
                results.find { it.first == item.first } ?: Pair(item.first, 0)
            when (item.second) {
                0f -> {
                    assertEquals(0, result.second)
                }
                1f -> {
                    assertEquals(iterations, result.second)
                }
                else -> {
                    val errorRange = 0.05f
                    val minFloat = max(weight - errorRange, 0f)
                    val maxFloat = min(weight + errorRange, 1f)
                    val minMatch = (minFloat * iterations).toInt()
                    val maxMatch = (maxFloat * iterations).toInt()
                    assert(result.second in (minMatch..maxMatch))
                }
            }
        }
    }
}