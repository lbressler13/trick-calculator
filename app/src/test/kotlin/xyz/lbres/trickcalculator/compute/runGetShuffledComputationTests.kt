package xyz.lbres.trickcalculator.compute

import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.runTestWithRetry
import xyz.lbres.trickcalculator.utils.isNumber
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val fullOps = listOf("+", "-", "x", "/", "^")
private const val iterations = 20

fun runGetShuffledComputationTests() {
    // values that won't change
    var text: StringList = emptyList()
    repeat(iterations) { assertEquals(text, getShuffledComputation(text, fullOps)) }

    text = listOf("123")
    repeat(iterations) { assertEquals(text, getShuffledComputation(text, fullOps)) }

    text = "( 100.5 )".split(' ')
    repeat(iterations) { assertEquals(text, getShuffledComputation(text, fullOps)) }

    text = listOf("+")
    repeat(iterations) { assertEquals(text, getShuffledComputation(text, fullOps)) }

    text = "5 + 5 + 5 + ( 5 + 5 ) + 5".split(' ')
    repeat(iterations) { assertEquals(text, getShuffledComputation(text, fullOps)) }

    // tests with full set of ops
    text = "1 + 3".split(' ') // just shuffle numbers
    runSingleGetShuffledComputationTest(text)

    text = "12 + 12 ^ 12 - 12 x 12".split(' ') // just shuffle ops
    runSingleGetShuffledComputationTest(text)

    text = "1.5 - 2 / 1000 ^ 3 x 18".split(' ') // shuffle both
    runSingleGetShuffledComputationTest(text)

    text = "8 x ( 55 - 9 + ( 3 ) - 4 )".split(' ') // shuffle with parens
    runSingleGetShuffledComputationTest(text)

    // tests with partial ops
    val partialOps = listOf("+", "-")

    text = "( 20 x 20 ) / 20".split(' ') // no operators
    repeat(iterations) { assertEquals(text, getShuffledComputation(text, partialOps)) }

    text = "9.8 x 9.8 / 9.8 + 9.8".split(' ') // one operator
    repeat(iterations) { assertEquals(text, getShuffledComputation(text, partialOps)) }

    text = "100 + 27 / 15".split(' ') // just shuffle numbers
    runSingleGetShuffledComputationTest(text, partialOps)

    text = "20 + 20 - 20".split(' ') // just shuffle ops
    runSingleGetShuffledComputationTest(text, partialOps)

    text = "( 103 x 27 ) - 104 + 27 + 13 - 0".split(' ') // shuffle both
    runSingleGetShuffledComputationTest(text, partialOps)
}

private fun runSingleGetShuffledComputationTest(text: StringList, ops: StringList = fullOps) {
    val opsType = "operator"
    val numType = "number"

    val mapping = text.map {
        when {
            isOperator(it, ops) -> opsType
            isNumber(it) -> numType
            else -> it
        }
    }

    val expectedSorted = text.sorted()

    val shuffleList = {
        val result = getShuffledComputation(text, ops)

        assertEquals(expectedSorted, result.sorted()) // contains same values

        // check that numbers and operators are in the correct places
        mapping.forEachIndexed { index, expectedType ->
            val newValue = result[index]
            when {
                isOperator(newValue, ops) -> assertEquals(expectedType, opsType)
                isNumber(newValue) -> assertEquals(expectedType, numType)
                else -> assertEquals(expectedType, newValue)
            }
        }

        result
    }

    val distinctElements = text.toSet()
    val distinctResults: MutableSet<StringList> = mutableSetOf()

    val minResults = when {
        distinctElements.size < 3 -> 1
        distinctElements.size < 5 -> 2
        distinctElements.size > 8 -> 4
        else -> 3
    }

    runTestWithRetry(tries = 20) {
        assertTrue {
            val result = shuffleList()
            distinctResults.add(result)

            result != text && distinctResults.size >= minResults
        }
    }
}
