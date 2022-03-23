package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction

fun runTermIsZeroTests() {
    var t = Term(ExactFraction.ZERO, 0)
    assert(t.isZero())

    t = Term(ExactFraction.ZERO, -5)
    assert(t.isZero())

    t = Term(ExactFraction.ZERO, 100)
    assert(t.isZero())

    t = Term(ExactFraction.FIVE, 0)
    assert(!t.isZero())

    t = Term(-ExactFraction.FIVE, 12)
    assert(!t.isZero())

    t = Term(ExactFraction(7, 13), 0)
    assert(!t.isZero())
}

fun runTermIsNotZeroTests() {
    var t = Term(ExactFraction.FIVE, 0)
    assert(t.isNotZero())

    t = Term(-ExactFraction.FIVE, 12)
    assert(t.isNotZero())

    t = Term(ExactFraction(7, 13), 0)
    assert(t.isNotZero())

    t = Term(ExactFraction.ZERO, 0)
    assert(!t.isNotZero())

    t = Term(ExactFraction.ZERO, -5)
    assert(!t.isNotZero())

    t = Term(ExactFraction.ZERO, 100)
    assert(!t.isNotZero())
}

fun runTermIsNegativeTests() {
    var t = Term(ExactFraction.ZERO, 0)
    assert(!t.isNegative())

    t = Term(ExactFraction.FIVE, 3)
    assert(!t.isNegative())

    t = Term(ExactFraction.FIVE, -3)
    assert(!t.isNegative())

    t = Term(-ExactFraction.FIVE, 3)
    assert(t.isNegative())

    t = Term(-ExactFraction.FIVE, -3)
    assert(t.isNegative())

    t = Term(ExactFraction(3, 7), 3)
    assert(!t.isNegative())

    t = Term(ExactFraction(3, 7), -3)
    assert(!t.isNegative())

    t = Term(ExactFraction(-3, 7), 3)
    assert(t.isNegative())

    t = Term(ExactFraction(-3, 7), -3)
    assert(t.isNegative())
}
