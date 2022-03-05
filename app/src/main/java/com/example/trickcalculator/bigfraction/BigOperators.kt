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

operator fun Int.plus(other: BigFraction): BigFraction = toBF() + other
operator fun Int.minus(other: BigFraction): BigFraction = toBF() - other
operator fun Int.times(other: BigFraction): BigFraction = toBF() * other
operator fun Int.div(other: BigFraction): BigFraction = toBF() / other

operator fun Int.plus(other: BigInteger): BigInteger = toBI() + other
operator fun Int.minus(other: BigInteger): BigInteger = toBI() - other
operator fun Int.times(other: BigInteger): BigInteger = toBI() * other
operator fun Int.div(other: BigInteger): BigInteger = toBI() / other

operator fun Long.plus(other: BigFraction): BigFraction = toBF() + other
operator fun Long.minus(other: BigFraction): BigFraction = toBF() - other
operator fun Long.times(other: BigFraction): BigFraction = toBF() * other
operator fun Long.div(other: BigFraction): BigFraction = toBF() / other

operator fun Long.plus(other: BigInteger): BigInteger = toBI() + other
operator fun Long.minus(other: BigInteger): BigInteger = toBI() - other
operator fun Long.times(other: BigInteger): BigInteger = toBI() * other
operator fun Long.div(other: BigInteger): BigInteger = toBI() / other

operator fun BigInteger.plus(other: BigFraction): BigFraction = toBF() + other
operator fun BigInteger.minus(other: BigFraction): BigFraction = toBF() - other
operator fun BigInteger.times(other: BigFraction): BigFraction = toBF() * other
operator fun BigInteger.div(other: BigFraction): BigFraction = toBF() / other

operator fun BigInteger.plus(other: Int): BigInteger = this + other.toBI()
operator fun BigInteger.minus(other: Int): BigInteger = this - other.toBI()
operator fun BigInteger.times(other: Int): BigInteger = this * other.toBI()
operator fun BigInteger.div(other: Int): BigInteger = this / other.toBI()

operator fun BigInteger.plus(other: Long): BigInteger = this + other.toBI()
operator fun BigInteger.minus(other: Long): BigInteger = this - other.toBI()
operator fun BigInteger.times(other: Long): BigInteger = this * other.toBI()
operator fun BigInteger.div(other: Long): BigInteger = this / other.toBI()

fun BigInteger.eq(other: Int): Boolean = other == toInt()
fun BigInteger.eq(other: Long): Boolean = other == toLong()
fun BigInteger.eq(other: BigFraction) = other == toBF()

operator fun BigInteger.compareTo(other: BigFraction): Int = -other.compareTo(this)

operator fun BigInteger.compareTo(other: Long): Int {
    val difference = this - other
    return when {
        difference.isNegative() -> -1
        difference.isZero() -> 0
        else -> 1
    }
}
operator fun BigInteger.compareTo(other: Int): Int = compareTo(other.toLong())

fun BigInteger.isNegative(): Boolean = this < BigInteger.ZERO
fun BigInteger.isZero(): Boolean = this == BigInteger.ZERO
