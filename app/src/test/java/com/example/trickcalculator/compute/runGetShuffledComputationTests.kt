package com.example.trickcalculator.compute

import com.example.trickcalculator.runRandomTest
import com.example.trickcalculator.repeat
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.isNumber
import org.junit.Assert.*

private val fullOps = listOf("+", "-", "x", "/", "^")

fun runGetShuffledComputationTests() {
    // values that won't change
    var text: StringList = listOf()
    repeat { assertEquals(text, getShuffledComputation(text, fullOps)) }

    text = listOf("123")
    repeat { assertEquals(text, getShuffledComputation(text, fullOps)) }

    text = "( 100.5 )".split(' ')
    repeat { assertEquals(text, getShuffledComputation(text, fullOps)) }

    text = listOf("+")
    repeat { assertEquals(text, getShuffledComputation(text, fullOps)) }

    text = "5 + 5 + 5 + ( 5 + 5 ) + 5".split(' ')
    repeat { assertEquals(text, getShuffledComputation(text, fullOps)) }

    // tests with full set of ops
    text = "1 + 3".split(' ') // just shuffle numbers
    runSingleGetShuffledComputationTest(text)

    text = "12 + 12 ^ 12 - 12 x 12".split(' ') //just shuffle ops
    runSingleGetShuffledComputationTest(text)

    text = "1.5 - 2 / 1000 ^ 3 x 18".split(' ') // shuffle both
    runSingleGetShuffledComputationTest(text)

    text = "8 x ( 55 - 9 + ( 3 ) - 4 )".split(' ') // shuffle with parens
    runSingleGetShuffledComputationTest(text)

    // tests with partial ops
    val partialOps = listOf("+", "-")

    text = "( 20 x 20 ) / 20".split(' ') // no operators
    repeat { assertEquals(text, getShuffledComputation(text, partialOps)) }

    text = "9.8 x 9.8 / 9.8 + 9.8".split(' ') // one operator
    repeat { assertEquals(text, getShuffledComputation(text, partialOps)) }

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

    val checkShuffled: (StringList) -> Boolean = { it != text }

    runRandomTest(shuffleList, checkShuffled)
}
