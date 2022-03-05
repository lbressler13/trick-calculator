package com.example.trickcalculator.bigfraction

import org.junit.Assert

fun runSimplifyTests() {
    // simplify 0
    var bf = BigFraction(0, 2)
    var expected = BigFraction(0)
    Assert.assertEquals(expected, bf)

    bf = BigFraction(0, -6)
    expected = BigFraction(0)
    Assert.assertEquals(expected, bf)

    // simplify exact division
    bf = BigFraction(4, 2)
    expected = BigFraction(2)
    Assert.assertEquals(expected, bf)

    bf = BigFraction(2, 4)
    expected = BigFraction(1, 2)
    Assert.assertEquals(expected, bf)

    // simplify sign
    bf = BigFraction(-3, -4)
    expected = BigFraction(3, 4)
    Assert.assertEquals(expected, bf)

    bf = BigFraction(1, -3)
    expected = BigFraction(-1, 3)
    Assert.assertEquals(expected, bf)

    // multiple simplifications
    bf = BigFraction(3, -9)
    expected = BigFraction(-1, 3)
    Assert.assertEquals(expected, bf)

    // unchanged
    bf = BigFraction(0)
    expected = BigFraction(0)
    Assert.assertEquals(expected, bf)

    bf = BigFraction(-4, 3)
    expected = BigFraction(-4, 3)
    Assert.assertEquals(expected, bf)

    bf = BigFraction(5, 7)
    expected = BigFraction(5, 7)
    Assert.assertEquals(expected, bf)
}

fun runSetSignTests() {
    var bf = BigFraction(-3, -4)
    var expected = BigFraction(3, 4)
    Assert.assertEquals(expected, bf)

    bf = BigFraction(1, -3)
    expected = BigFraction(-1, 3)
    Assert.assertEquals(expected, bf)

    bf = BigFraction(1, 3)
    expected = BigFraction(1, 3)
    Assert.assertEquals(expected, bf)

    bf = BigFraction(-5, 2)
    expected = BigFraction(-5, 2)
    Assert.assertEquals(expected, bf)
}
