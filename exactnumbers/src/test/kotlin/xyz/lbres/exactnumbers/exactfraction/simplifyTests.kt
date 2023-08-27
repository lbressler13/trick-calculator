package xyz.lbres.exactnumbers.exactfraction

import xyz.lbres.exactnumbers.ext.eq

fun runSimplifyTests() {
    runSimplifyZeroTests()
    runSimplifyGCDTests()
    runSimplifySignTests()

    // multiple simplifications
    var ef = ExactFraction(3, -9)
    assert(ef.numerator.eq(-1))
    assert(ef.denominator.eq(3))

    ef = ExactFraction(-18, -12)
    assert(ef.numerator.eq(3))
    assert(ef.denominator.eq(2))

    // unchanged
    ef = ExactFraction(0)
    assert(ef.numerator.eq(0))
    assert(ef.denominator.eq(1))

    ef = ExactFraction(-4, 3)
    assert(ef.numerator.eq(-4))
    assert(ef.denominator.eq(3))

    ef = ExactFraction(5, 7)
    assert(ef.numerator.eq(5))
    assert(ef.denominator.eq(7))
}

private fun runSimplifyZeroTests() {
    var ef = ExactFraction(0, 2)
    assert(ef.numerator.eq(0))
    assert(ef.denominator.eq(1))

    ef = ExactFraction(0, 6)
    assert(ef.numerator.eq(0))
    assert(ef.denominator.eq(1))

    ef = ExactFraction(0, -6)
    assert(ef.numerator.eq(0))
    assert(ef.denominator.eq(1))
}

private fun runSimplifySignTests() {
    var ef = ExactFraction(-3, -4)
    assert(ef.numerator.eq(3))
    assert(ef.denominator.eq(4))

    ef = ExactFraction(1, -3)
    assert(ef.numerator.eq(-1))
    assert(ef.denominator.eq(3))

    ef = ExactFraction(1, 3)
    assert(ef.numerator.eq(1))
    assert(ef.denominator.eq(3))

    ef = ExactFraction(-5, 2)
    assert(ef.numerator.eq(-5))
    assert(ef.denominator.eq(2))
}

private fun runSimplifyGCDTests() {
    var ef = ExactFraction(48, 10)
    assert(ef.numerator.eq(24))
    assert(ef.denominator.eq(5))

    ef = ExactFraction(-462, 1071)
    assert(ef.numerator.eq(-22))
    assert(ef.denominator.eq(51))

    ef = ExactFraction(5, 9)
    assert(ef.numerator.eq(5))
    assert(ef.denominator.eq(9))

    ef = ExactFraction(9, 3)
    assert(ef.numerator.eq(3))
    assert(ef.denominator.eq(1))

    ef = ExactFraction(10, 100)
    assert(ef.numerator.eq(1))
    assert(ef.denominator.eq(10))

    ef = ExactFraction(2.toBigInteger(), 2.toBigInteger())
    assert(ef.numerator.eq(1))
    assert(ef.denominator.eq(1))
}
