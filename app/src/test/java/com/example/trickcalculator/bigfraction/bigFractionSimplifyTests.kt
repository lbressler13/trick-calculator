package com.example.trickcalculator.bigfraction

fun runSimplifyTests() {
    runSimplifyZeroTests()
    runSimplifyGCDTests()
    runSimplifySignTests()

    // multiple simplifications
    var bf = BigFraction(3, -9)
    assert(bf.numerator.eq(-1))
    assert(bf.denominator.eq(3))

    bf = BigFraction(-18, -12)
    assert(bf.numerator.eq(3))
    assert(bf.denominator.eq(2))

    // unchanged
    bf = BigFraction(0)
    assert(bf.numerator.eq(0))
    assert(bf.denominator.eq(1))

    bf = BigFraction(-4, 3)
    assert(bf.numerator.eq(-4))
    assert(bf.denominator.eq(3))

    bf = BigFraction(5, 7)
    assert(bf.numerator.eq(5))
    assert(bf.denominator.eq(7))
}

private fun runSimplifyZeroTests() {
    var bf = BigFraction(0, 2)
    assert(bf.numerator.eq(0))
    assert(bf.denominator.eq(1))

    bf = BigFraction(0, 6)
    assert(bf.numerator.eq(0))
    assert(bf.denominator.eq(1))

    bf = BigFraction(0, -6)
    assert(bf.numerator.eq(0))
    assert(bf.denominator.eq(1))
}

private fun runSimplifySignTests() {
    var bf = BigFraction(-3, -4)
    assert(bf.numerator.eq(3))
    assert(bf.denominator.eq(4))

    bf = BigFraction(1, -3)
    assert(bf.numerator.eq(-1))
    assert(bf.denominator.eq(3))

    bf = BigFraction(1, 3)
    assert(bf.numerator.eq(1))
    assert(bf.denominator.eq(3))

    bf = BigFraction(-5, 2)
    assert(bf.numerator.eq(-5))
    assert(bf.denominator.eq(2))
}

private fun runSimplifyGCDTests() {
    var bf = BigFraction(48, 10)
    assert(bf.numerator.eq(24))
    assert(bf.denominator.eq(5))

    bf = BigFraction(-462, 1071)
    assert(bf.numerator.eq(-22))
    assert(bf.denominator.eq(51))

    bf = BigFraction(5, 9)
    assert(bf.numerator.eq(5))
    assert(bf.denominator.eq(9))

    bf = BigFraction(9, 3)
    assert(bf.numerator.eq(3))
    assert(bf.denominator.eq(1))

    bf = BigFraction(10, 100)
    assert(bf.numerator.eq(1))
    assert(bf.denominator.eq(10))
}
