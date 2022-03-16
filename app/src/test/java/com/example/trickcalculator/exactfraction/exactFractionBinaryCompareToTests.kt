package com.example.trickcalculator.exactfraction

import com.example.trickcalculator.ext.toBI
import java.math.BigInteger

fun runCompareToTests() {
    // equal values
    var first = ExactFraction(0)
    var second = ExactFraction(0)
    assert(first <= second)
    assert(first >= second)

    first = ExactFraction(100)
    second = ExactFraction(100)
    assert(first <= second)
    assert(first >= second)

    // pos less than zero
    first = ExactFraction(3)
    second = ExactFraction(0)
    assert(first > second)
    assert(first >= second)
    assert(second < first)
    assert(second <= first)

    // neg less than zero
    first = ExactFraction(-3)
    second = ExactFraction(0)
    assert(first < second)
    assert(first <= second)
    assert(second > first)
    assert(second >= first)

    // pos less than neg
    first = ExactFraction(-1)
    second = ExactFraction(1)
    assert(first < second)
    assert(first <= second)
    assert(second > first)
    assert(second >= first)

    // neg order
    first = ExactFraction(-3)
    second = ExactFraction(-2)
    assert(first < second)
    assert(first <= second)
    assert(second > first)
    assert(second >= first)

    // pos order
    first = ExactFraction(3)
    second = ExactFraction(2)
    assert(first > second)
    assert(first >= second)
    assert(second < first)
    assert(second <= first)

    // BigInteger
    var ef = ExactFraction(0)
    var bi = BigInteger.ZERO
    assert(ef <= bi)
    assert(ef >= bi)

    ef = ExactFraction(100)
    bi = 100.toBI()
    assert(ef <= bi)
    assert(ef >= bi)

    ef = ExactFraction(3)
    bi = BigInteger.ZERO
    assert(ef > bi)
    assert(ef >= bi)

    ef = ExactFraction(-3)
    bi = BigInteger.ZERO
    assert(ef < bi)
    assert(ef <= bi)

    ef = ExactFraction(-1)
    bi = BigInteger.ONE
    assert(ef < bi)
    assert(ef <= bi)

    ef = ExactFraction(-3)
    bi = (-2).toBI()
    assert(ef < bi)
    assert(ef <= bi)

    ef = ExactFraction(3)
    bi = 2.toBI()
    assert(ef > bi)
    assert(ef >= bi)

    // Int
    ef = ExactFraction(0)
    var i = 0
    assert(ef <= i)
    assert(ef >= i)

    ef = ExactFraction(100)
    i = 100
    assert(ef <= i)
    assert(ef >= i)

    ef = ExactFraction(3)
    i = 0
    assert(ef > i)
    assert(ef >= i)

    ef = ExactFraction(-3)
    i = 0
    assert(ef < i)
    assert(ef <= i)

    ef = ExactFraction(-1)
    i = 1
    assert(ef < i)
    assert(ef <= i)

    ef = ExactFraction(-3)
    i = -2
    assert(ef < i)
    assert(ef <= i)

    ef = ExactFraction(3)
    i = 2
    assert(ef > i)
    assert(ef >= i)

    // Long
    ef = ExactFraction(0)
    var l = 0L
    assert(ef <= l)
    assert(ef >= l)

    ef = ExactFraction(100)
    l = 100L
    assert(ef <= l)
    assert(ef >= l)

    ef = ExactFraction(3)
    l = 0L
    assert(ef > l)
    assert(ef >= i)

    ef = ExactFraction(-3)
    l = 0L
    assert(ef < l)
    assert(ef <= l)

    ef = ExactFraction(-1)
    l = 1L
    assert(ef < l)
    assert(ef <= l)

    ef = ExactFraction(-3)
    l = -2L
    assert(ef < l)
    assert(ef <= l)

    ef = ExactFraction(3)
    l = 2L
    assert(ef > l)
    assert(ef >= l)
}
