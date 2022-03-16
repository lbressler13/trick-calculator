package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.bigfraction.BigFraction
import org.junit.Assert.*

fun runPlusTests() {
}

fun runMinusTests() {

}

fun runTimesTests() {
    var ed1 = ExactDecimal(listOf("1"), listOf("2"), BigFraction(1, 5))
    var ed2 = ExactDecimal(listOf("pi", "pi + 1"), listOf("3"), BigFraction(2, 7))
    var expected = ExactDecimal(
        listOf("1", "pi", "pi + 1"),
        listOf("2", "3"),
        BigFraction(2, 35)
    )
    assertEquals(expected, ed1 * ed2)
}

fun runDivTests() {

}

fun runEqualsTests() {

}