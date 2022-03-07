package com.example.trickcalculator.bigfraction

import java.math.BigInteger

fun Int.toBI(): BigInteger = toBigInteger()
fun Int.toBigFraction(): BigFraction = BigFraction(this)
fun Int.toBF(): BigFraction = toBigFraction()

fun Long.toBI(): BigInteger = toBigInteger()
fun Long.toBigFraction(): BigFraction = BigFraction(this)
fun Long.toBF(): BigFraction = toBigFraction()

fun BigInteger.toBigFraction(): BigFraction = BigFraction(this)
fun BigInteger.toBF(): BigFraction = toBigFraction()

fun BigInteger.eq(other: Int): Boolean = other == toInt()
fun BigInteger.eq(other: Long): Boolean = other == toLong()

fun BigInteger.isNegative(): Boolean = this < BigInteger.ZERO
fun BigInteger.isZero(): Boolean = this == BigInteger.ZERO
