package xyz.lbres.exactnumbers.irrationals.common

import java.math.BigInteger

/**
 * Object to memoize values that require high computation
 */
internal object Memoize {
    /**
     * Result of Sqrt extractWholeOf function.
     * Function relies on prime factorization of the input value.
     */
    val individualWholeNumber: MutableMap<BigInteger, BigInteger> = mutableMapOf()
}
