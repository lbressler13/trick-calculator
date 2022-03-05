package com.example.trickcalculator.bigfraction

import org.junit.Assert.*

fun runConstructorTests() {
    var bf = BigFraction(0)
    assert(bf.numerator.eq(0))
    assert(bf.denominator.eq(1))

    bf = BigFraction(3)
    assert(bf.numerator.eq(3))
    assert(bf.denominator.eq(1))

    bf = BigFraction(-3)
    assert(bf.numerator.eq(-3))
    assert(bf.denominator.eq(1))

    bf = BigFraction(3, 4)
    assert(bf.numerator.eq(3))
    assert(bf.denominator.eq(4))

    bf = BigFraction(3, -2)
    assert(bf.numerator.eq(-3))
    assert(bf.denominator.eq(2))

    var error = ""
    try {
        BigFraction(1, 0)
    } catch (e: ArithmeticException) {
        error = e.message.toString()
    }
    assertEquals("divide by zero", error)
}
