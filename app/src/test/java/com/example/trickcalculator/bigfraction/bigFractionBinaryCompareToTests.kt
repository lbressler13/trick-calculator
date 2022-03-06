package com.example.trickcalculator.bigfraction

import java.math.BigInteger

fun runCompareToTests() {
    // equal values
    var first = BigFraction(0)
    var second = BigFraction(0)
    assert(first <= second)
    assert(first >= second)

    first = BigFraction(100)
    second = BigFraction(100)
    assert(first <= second)
    assert(first >= second)

    // pos less than zero
    first = BigFraction(3)
    second = BigFraction(0)
    assert(first > second)
    assert(first >= second)
    assert(second < first)
    assert(second <= first)

    // neg less than zero
    first = BigFraction(-3)
    second = BigFraction(0)
    assert(first < second)
    assert(first <= second)
    assert(second > first)
    assert(second >= first)

    // pos less than neg
    first = BigFraction(-1)
    second = BigFraction(1)
    assert(first < second)
    assert(first <= second)
    assert(second > first)
    assert(second >= first)

    // neg order
    first = BigFraction(-3)
    second = BigFraction(-2)
    assert(first < second)
    assert(first <= second)
    assert(second > first)
    assert(second >= first)

    // pos order
    first = BigFraction(3)
    second = BigFraction(2)
    assert(first > second)
    assert(first >= second)
    assert(second < first)
    assert(second <= first)

    // BigInteger
    var bf = BigFraction(0)
    var bi = BigInteger.ZERO
    assert(bf <= bi)
    assert(bf >= bi)

    bf = BigFraction(100)
    bi = 100.toBI()
    assert(bf <= bi)
    assert(bf >= bi)

    bf = BigFraction(3)
    bi = BigInteger.ZERO
    assert(bf > bi)
    assert(bf >= bi)

    bf = BigFraction(-3)
    bi = BigInteger.ZERO
    assert(bf < bi)
    assert(bf <= bi)

    bf = BigFraction(-1)
    bi = BigInteger.ONE
    assert(bf < bi)
    assert(bf <= bi)

    bf = BigFraction(-3)
    bi = (-2).toBI()
    assert(bf < bi)
    assert(bf <= bi)

    bf = BigFraction(3)
    bi = 2.toBI()
    assert(bf > bi)
    assert(bf >= bi)

    // Int
    bf = BigFraction(0)
    var i = 0
    assert(bf <= i)
    assert(bf >= i)

    bf = BigFraction(100)
    i = 100
    assert(bf <= i)
    assert(bf >= i)

    bf = BigFraction(3)
    i = 0
    assert(bf > i)
    assert(bf >= i)

    bf = BigFraction(-3)
    i = 0
    assert(bf < i)
    assert(bf <= i)

    bf = BigFraction(-1)
    i = 1
    assert(bf < i)
    assert(bf <= i)

    bf = BigFraction(-3)
    i = -2
    assert(bf < i)
    assert(bf <= i)

    bf = BigFraction(3)
    i = 2
    assert(bf > i)
    assert(bf >= i)

    // Long
    bf = BigFraction(0)
    var l = 0L
    assert(bf <= l)
    assert(bf >= l)

    bf = BigFraction(100)
    l = 100L
    assert(bf <= l)
    assert(bf >= l)

    bf = BigFraction(3)
    l = 0L
    assert(bf > l)
    assert(bf >= i)

    bf = BigFraction(-3)
    l = 0L
    assert(bf < l)
    assert(bf <= l)

    bf = BigFraction(-1)
    l = 1L
    assert(bf < l)
    assert(bf <= l)

    bf = BigFraction(-3)
    l = -2L
    assert(bf < l)
    assert(bf <= l)

    bf = BigFraction(3)
    l = 2L
    assert(bf > l)
    assert(bf >= l)
}
