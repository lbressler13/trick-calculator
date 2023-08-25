package xyz.lbres.common

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

/**
 * Perform division of two BigDecimals, rounding to 20 digits if the result is irrational
 *
 * @param bigDec1 [BigDecimal]: number on left side of division
 * @param bigDec2 [BigDecimal]: number on right side of division
 * @return [BigDecimal]: bigDec1 / bigDec2
 */
internal fun divideBigDecimals(bigDec1: BigDecimal, bigDec2: BigDecimal): BigDecimal {
    return try {
        bigDec1.divide(bigDec2)
    } catch (e: ArithmeticException) {
        val mc = MathContext(20)
        bigDec1.divide(bigDec2, mc)
    }
}

/**
 * Round decimal to nearest ints, and determine if either value passes a check.
 * If so, returns the passing value. If both pass, the closer value will be returned.
 *
 * @param decimal [BigDecimal]: the number to round
 * @param checkInt [(BigInteger) -> Boolean]: function to check rounded value
 * @return [BigInteger?]: a rounded number that passes the checks, or null if there is none
 */
internal fun getIntFromDecimal(decimal: BigDecimal, checkInt: (BigInteger) -> Boolean): BigInteger? {
    try {
        val upInt = decimal.setScale(0, RoundingMode.UP).toBigInteger()
        val downInt = decimal.setScale(0, RoundingMode.DOWN).toBigInteger()

        val upPasses = checkInt(upInt)
        val downPasses = checkInt(downInt)

        return when {
            upPasses && downPasses -> decimal.setScale(0, RoundingMode.HALF_UP).toBigInteger()
            upPasses -> upInt
            downPasses -> downInt
            else -> null
        }
    } catch (_: Exception) {
        return null
    }
}
