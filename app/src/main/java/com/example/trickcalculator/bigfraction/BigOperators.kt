package com.example.trickcalculator.bigfraction

import java.math.BigInteger

// Additional ext methods related to BigFraction and BigInteger

// casting methods for Int
fun Int.toBI(): BigInteger = toBigInteger()
fun Int.toBigFraction(): BigFraction = BigFraction(this)
fun Int.toBF(): BigFraction = toBigFraction()

// casting methods for Long
fun Long.toBI(): BigInteger = toBigInteger()
fun Long.toBigFraction(): BigFraction = BigFraction(this)
fun Long.toBF(): BigFraction = toBigFraction()

// casting methods for BigInteger
fun BigInteger.toBigFraction(): BigFraction = BigFraction(this)
fun BigInteger.toBF(): BigFraction = toBigFraction()

// equality of BigInteger and standard number formats
fun BigInteger.eq(other: Int): Boolean = other == toInt()
fun BigInteger.eq(other: Long): Boolean = other == toLong()

// unary checks for BigInteger
fun BigInteger.isNegative(): Boolean = this < BigInteger.ZERO
fun BigInteger.isZero(): Boolean = this == BigInteger.ZERO
