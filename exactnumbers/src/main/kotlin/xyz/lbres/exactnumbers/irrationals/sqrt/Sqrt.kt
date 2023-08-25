package xyz.lbres.exactnumbers.irrationals.sqrt

import xyz.lbres.exactnumbers.common.divideBigDecimals
import xyz.lbres.exactnumbers.common.divideByZero
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.common.Irrational
import xyz.lbres.exactnumbers.irrationals.log.Log
import xyz.lbres.exactnumbers.irrationals.pi.Pi
import xyz.lbres.exactnumbers.expressions.term.Term
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Representation of a square root with a rational radicand
 *
 * @param radicand [ExactFraction]: value to compute root of
 * @param fullySimplified [Boolean]: if the value has already been simplified, such that getSimplified will return the same value
 * @throws [ArithmeticException] if radicand is negative
 */
class Sqrt private constructor(val radicand: ExactFraction, private val fullySimplified: Boolean) : Comparable<Sqrt>, Irrational {
    override val type = TYPE
    override val isDivided = false

    init {
        if (radicand.isNegative()) {
            throw ArithmeticException("Cannot calculate root of a negative number")
        }
    }

    // constructors with reduced params + other types
    constructor(radicand: ExactFraction) : this(radicand, false)

    constructor(radicand: Int) : this(ExactFraction(radicand), false)
    constructor(radicand: Long) : this(ExactFraction(radicand), false)
    constructor(radicand: BigInteger) : this(ExactFraction(radicand), false)
    private constructor(radicand: Int, fullySimplified: Boolean) : this(
        ExactFraction(
            radicand
        ), fullySimplified)
    private constructor(radicand: Long, fullySimplified: Boolean) : this(
        ExactFraction(
            radicand
        ), fullySimplified)
    private constructor(radicand: BigInteger, fullySimplified: Boolean) : this(
        ExactFraction(
            radicand
        ), fullySimplified)

    // public methods to expose general Irrational operators
    operator fun times(other: Sqrt): Term = times(other)
    operator fun times(other: Log): Term = times(other)
    operator fun times(other: Pi): Term = times(other)
    operator fun div(other: Sqrt): Term = div(other)
    operator fun div(other: Log): Term = div(other)
    operator fun div(other: Pi): Term = div(other)

    override fun isZero(): Boolean = radicand.isZero()
    override fun swapDivided(): Sqrt {
        if (isZero()) {
            throw divideByZero
        }

        return Sqrt(radicand.inverse())
    }

    /**
     * Determine if the value of the root is a rational number.
     *
     * @return [Boolean]: true if the value is rational, false otherwise
     */
    override fun isRational(): Boolean {
        val numRoot = getRootOf(radicand.numerator).toPlainString()
        val denomRoot = getRootOf(radicand.denominator).toPlainString()

        return numRoot.indexOf('.') == -1 && denomRoot.indexOf('.') == -1
    }

    /**
     * Get the value of the root as a rational value if rational
     *
     * @return [ExactFraction?]: value of the root, or null if the value is irrational
     */
    override fun getRationalValue(): ExactFraction? {
        if (!isRational()) {
            return null
        }

        val numRoot = getRootOf(radicand.numerator).toBigInteger()
        val denomRoot = getRootOf(radicand.denominator).toBigInteger()
        return ExactFraction(numRoot, denomRoot)
    }

    /**
     * Get value of root, using the formula sqrt(x/y) = sqrt(x)/sqrt(y).
     * This reduces loss of precision when casting to Double.
     *
     * @return [BigDecimal]
     */
    override fun getValue(): BigDecimal {
        val numRoot = getRootOf(radicand.numerator)
        val denomRoot = getRootOf(radicand.denominator)
        return divideBigDecimals(numRoot, denomRoot)
    }

    override fun equals(other: Any?): Boolean = other != null && other is Sqrt && radicand == other.radicand

    /**
     * Simplify log into a coefficient and a root.
     * Extracts rational component of root into coefficient, and leaves remaining piece as root.
     * For example sqrt(50) returns coefficient 5 and sqrt(2)
     *
     * @return [Pair<ExactFraction, Sqrt>]: a pair of coefficient and sqrt such that the product has the same value as the current sqrt
     */
    fun getSimplified(): Pair<ExactFraction, Sqrt> {
        if (fullySimplified) {
            return Pair(ExactFraction.ONE, this)
        }

        if (radicand.isZero()) {
            return Pair(ExactFraction.ONE, Sqrt(ExactFraction.ZERO, true))
        }

        if (radicand == ExactFraction.ONE) {
            return Pair(ExactFraction.ONE, Sqrt(ExactFraction.ONE, true))
        }

        val numWhole = extractWholeOf(radicand.numerator)
        val denomWhole = extractWholeOf(radicand.denominator)
        val whole = ExactFraction(numWhole, denomWhole)

        val newNum = radicand.numerator / (numWhole * numWhole)
        val newDenom = radicand.denominator / (denomWhole * denomWhole)
        val newRadicand = ExactFraction(newNum, newDenom)

        return Pair(whole, Sqrt(newRadicand, true))
    }

    override fun compareTo(other: Sqrt): Int = radicand.compareTo(other.radicand)

    override fun toString(): String {
        val numString = if (radicand.denominator == BigInteger.ONE) {
            radicand.numerator.toString()
        } else {
            "${radicand.numerator}/${radicand.denominator}"
        }

        return "[√($numString)]"
    }

    override fun hashCode(): Int = listOf(TYPE, radicand).hashCode()

    companion object {
        const val TYPE = "sqrt"

        val ZERO = Sqrt(ExactFraction.ZERO, fullySimplified = true)
        val ONE = Sqrt(ExactFraction.ONE, fullySimplified = true)

        /**
         * Extract rational values and simplify remaining list of irrationals
         *
         * @param numbers [List<Irrational>]: list to simplify, expected to consist of only Sqrts
         * @return [Pair<ExactFraction, List<Sqrt>>]: product of rational values and a list containing a single, fully simplified irrational root
         * @throws [ClassCastException] if any of the numbers are not a Sqrt
         */
        internal fun simplifyList(numbers: List<Irrational>?): Pair<ExactFraction, List<Sqrt>> {
            if (numbers.isNullOrEmpty()) {
                return Pair(ExactFraction.ONE, listOf())
            }

            @Suppress("UNCHECKED_CAST")
            numbers as List<Sqrt>

            if (numbers.any(Sqrt::isZero)) {
                return Pair(ExactFraction.ZERO, listOf())
            }

            // combine all roots into single root, and return that value
            val total = numbers.fold(ExactFraction.ONE) { acc, sqrt -> acc * sqrt.radicand }
            val numWhole = extractWholeOf(total.numerator)
            val denomWhole = extractWholeOf(total.denominator)
            val numRoot = total.numerator / (numWhole * numWhole)
            val denomRoot = total.denominator / (denomWhole * denomWhole)

            val root = Sqrt(ExactFraction(numRoot, denomRoot), true)
            val coeff = ExactFraction(numWhole, denomWhole)

            val rootList = if (root == ONE) {
                listOf()
            } else {
                listOf(root)
            }

            return Pair(coeff, rootList)
        }
    }
}
