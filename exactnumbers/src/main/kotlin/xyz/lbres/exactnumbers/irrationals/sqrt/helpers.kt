package xyz.lbres.exactnumbers.irrationals.sqrt

import xyz.lbres.exactnumbers.common.getIntFromDecimal
import xyz.lbres.exactnumbers.irrationals.common.Memoize
import xyz.lbres.kotlinutils.biginteger.ext.isNegative
import xyz.lbres.kotlinutils.biginteger.ext.isZero
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

/**
 * Find component of number that is a perfect square and can be extracted as a coefficient.
 * As an example, the function would return 5 for 50, because 50 = 2 * 5^2.
 * Uses memoization to avoid repeated computation.
 *
 * @param num [BigInteger]: value to extract from, required to be non-negative
 * @return [BigInteger]: the whole number that was extracted
 * @throws [ArithmeticException] if [num] is negative
 */
internal fun extractWholeOf(num: BigInteger): BigInteger {
    if (num.isNegative()) {
        throw ArithmeticException("Cannot calculate root of a negative number")
    }

    val memo = Memoize.individualWholeNumber

    fun addToMemo(key: BigInteger, value: BigInteger) {
        if (key !in memo) {
            memo[key] = value
        }
    }

    if (num in memo) {
        return memo[num]!!
    }

    if (num.isZero()) {
        addToMemo(num, BigInteger.ONE)
        return BigInteger.ONE
    }

    var extracted = BigInteger.ONE
    var factor = BigInteger.TWO
    var remaining = num

    val orderedRemaining = mutableListOf(remaining)
    val orderedFactors = mutableListOf(BigInteger.ONE)

    while (factor * factor <= remaining && remaining > BigInteger.ONE) {
        if (remaining in memo) {
            val fromMemo = memo[remaining]!!
            extracted *= fromMemo
            remaining = BigInteger.ONE

            orderedFactors.add(fromMemo)
            orderedRemaining.add(remaining)
        } else {
            // divide by current factor as many times as needed
            var extractedCount = 0

            while (remaining % (factor * factor) == BigInteger.ZERO) {
                extracted *= factor
                remaining /= (factor * factor)
                extractedCount++
            }

            if (extractedCount > 0) {
                orderedFactors.add(factor.pow(extractedCount))
                orderedRemaining.add(remaining)
                addToMemo(factor, BigInteger.ONE)
            }

            factor++
        }
    }

    var currentProduct = BigInteger.ONE
    for (idx in orderedFactors.indices.reversed()) {
        addToMemo(orderedRemaining[idx], currentProduct)
        currentProduct *= orderedFactors[idx]
    }

    return extracted
}

/**
 * Get sqrt value of a whole number
 *
 * @param num [BigInteger]: number to get root of
 * @return [BigDecimal]: the root of the number, using the current base
 */
internal fun getRootOf(num: BigInteger): BigDecimal {
    val mc = MathContext(20, RoundingMode.HALF_UP)
    val whole = extractWholeOf(num)
    val remaining = num / (whole * whole)
    val root = whole.toBigDecimal() * remaining.toBigDecimal().sqrt(mc)
    val int = getIntFromDecimal(root) { it * it == num }

    return int?.toBigDecimal() ?: root
}
