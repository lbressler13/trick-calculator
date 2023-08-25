package xyz.lbres.exactnumbers.ext

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import java.math.BigInteger

// Additional ext methods related to ExactFraction and BigInteger

// casting to ExactFraction
internal fun Int.toExactFraction(): ExactFraction = ExactFraction(this)
internal fun Long.toExactFraction(): ExactFraction = ExactFraction(this)
internal fun BigInteger.toExactFraction(): ExactFraction = ExactFraction(this)

// equality checks for BigInteger
internal fun BigInteger.eq(other: Int): Boolean = equals(other.toBigInteger())
internal fun BigInteger.eq(other: Long): Boolean = equals(other.toBigInteger())
