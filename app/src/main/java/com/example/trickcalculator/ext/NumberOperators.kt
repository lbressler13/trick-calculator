package com.example.trickcalculator.ext

import exactfraction.ExactFraction
import java.math.BigInteger

// Additional ext methods related to ExactFraction and BigInteger

// casting methods for Int
fun Int.toBI(): BigInteger = toBigInteger()
fun Int.toExactFraction(): ExactFraction = ExactFraction(this)
fun Int.toEF(): ExactFraction = toExactFraction()

// casting methods for Long
fun Long.toBI(): BigInteger = toBigInteger()
fun Long.toExactFraction(): ExactFraction = ExactFraction(this)
fun Long.toEF(): ExactFraction = toExactFraction()

// casting methods for BigInteger
fun BigInteger.toExactFraction(): ExactFraction = ExactFraction(this)
fun BigInteger.toEF(): ExactFraction = toExactFraction()

// equality of BigInteger and standard number formats
fun BigInteger.eq(other: Int): Boolean = other == toInt()
fun BigInteger.eq(other: Long): Boolean = other == toLong()

// unary checks for BigInteger
fun BigInteger.isNegative(): Boolean = this < BigInteger.ZERO
fun BigInteger.isZero(): Boolean = this == BigInteger.ZERO

fun min(val1: BigInteger, val2: BigInteger) = if (val1 < val2) val1 else val2
fun max(val1: BigInteger, val2: BigInteger) = if (val2 < val1) val1 else val2
