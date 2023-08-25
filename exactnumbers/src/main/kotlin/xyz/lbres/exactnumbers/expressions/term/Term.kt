package xyz.lbres.exactnumbers.expressions.term

import xyz.lbres.exactnumbers.common.divideBigDecimals
import xyz.lbres.exactnumbers.common.divideByZero
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.common.Irrational
import xyz.lbres.exactnumbers.irrationals.log.Log
import xyz.lbres.exactnumbers.irrationals.pi.Pi
import xyz.lbres.exactnumbers.irrationals.sqrt.Sqrt
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.abs

/**
 * Representation of the product of several numbers, represented as a rational coefficient and list of irrational numbers
 *
 * @param coefficient [ExactFraction]
 * @param numbers [List<Irrational>]
 */
class Term internal constructor(coefficient: ExactFraction, numbers: List<Irrational>) {
    val coefficient: ExactFraction
    internal val numbers: List<Irrational>

    init {
        if (coefficient.isZero() || numbers.any { it.isZero() }) {
            this.coefficient = ExactFraction.ZERO
            this.numbers = listOf()
        } else {
            this.coefficient = coefficient
            this.numbers = numbers
        }
    }

    operator fun unaryMinus(): Term = Term(-coefficient, numbers)
    operator fun unaryPlus(): Term = this

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Term) {
            return false
        }

        val simplified = getSimplified()
        val otherSimplified = other.getSimplified()

        return simplified.coefficient == otherSimplified.coefficient &&
            simplified.getPiCount() == otherSimplified.getPiCount() &&
            simplified.getLogs().sorted() == otherSimplified.getLogs().sorted() &&
            simplified.getSquareRoots().sorted() == otherSimplified.getSquareRoots().sorted()
    }

    operator fun times(other: Term): Term {
        val newCoeff = coefficient * other.coefficient
        val newNumbers = numbers + other.numbers
        return Term(newCoeff, newNumbers)
    }

    operator fun div(other: Term): Term {
        if (other.isZero()) {
            throw divideByZero
        }

        val newCoeff = coefficient / other.coefficient
        val newNumbers = numbers + other.numbers.map { it.swapDivided() }
        return Term(newCoeff, newNumbers)
    }

    fun isZero(): Boolean = coefficient.isZero() || numbers.any { it.isZero() }

    /**
     * Simplify all numbers, based on the simplify function for their type
     *
     * @return [Term] simplified version of this term
     */
    fun getSimplified(): Term {
        val groups = numbers.groupBy { it.type }
        val logs = Log.simplifyList(groups[Log.TYPE] ?: listOf())
        val pis = Pi.simplifyList(groups[Pi.TYPE] ?: listOf())
        val sqrts = Sqrt.simplifyList(groups[Sqrt.TYPE] ?: listOf())
        val newCoefficient = coefficient * logs.first * sqrts.first
        val newNumbers = logs.second + sqrts.second + pis

        return Term(newCoefficient, newNumbers)
    }

    /**
     * Get value of term by multiplying numbers.
     * Term is simplified before any computation is run
     *
     * @return [BigDecimal]
     */
    fun getValue(): BigDecimal {
        val simplified = getSimplified()

        val numbersProduct = simplified.numbers.fold(BigDecimal.ONE) { acc, num -> acc * num.getValue() }
        val numeratorProduct = numbersProduct * simplified.coefficient.numerator.toBigDecimal()

        return divideBigDecimals(numeratorProduct, simplified.coefficient.denominator.toBigDecimal())
    }

    /**
     * Get all logs from numbers
     */
    @Suppress("UNCHECKED_CAST")
    fun getLogs(): List<Log> = numbers.filter { it.type == Log.TYPE } as List<Log>

    /**
     * Get number of Pi in numbers. Divided Pi is counted as -1
     */
    fun getPiCount(): Int {
        val pis = numbers.filter { it.type == Pi.TYPE }
        val positive = pis.count { !it.isDivided }
        val negative = pis.size - positive
        return positive - negative
    }

    /**
     * Get all square roots from numbers
     */
    @Suppress("UNCHECKED_CAST")
    fun getSquareRoots(): List<Sqrt> = numbers.filter { it.type == Sqrt.TYPE } as List<Sqrt>

    override fun toString(): String {
        val coeffString = if (coefficient.denominator == BigInteger.ONE) {
            coefficient.numerator.toString()
        } else {
            "[${coefficient.numerator}/${coefficient.denominator}]"
        }

        val numString = numbers.joinToString("x")

        return if (numString.isEmpty()) {
            "<$coeffString>"
        } else {
            "<${coeffString}x$numString>"
        }
    }

    override fun hashCode(): Int = listOf("Term", coefficient, numbers).hashCode()

    companion object {
        val ZERO = Term(ExactFraction.ZERO, listOf())
        val ONE = Term(ExactFraction.ONE, listOf())

        /**
         * Public method of constructing a Term, by providing information about irrationals
         *
         * @param coefficient [ExactFraction]
         * @param logs [List<Log>]: list of log numbers
         * @param piCount [Int]: how many occurrence of Pi to include in the list of numbers.
         * A negative number corresponds to divided Pi values
         * @return [Term] with the given values
         */
        fun fromValues(coefficient: ExactFraction, logs: List<Log>, roots: List<Sqrt>, piCount: Int): Term {
            val piDivided = piCount < 0
            val piList = List(abs(piCount)) { Pi(isDivided = piDivided) }

            return Term(coefficient, logs + roots + piList)
        }
    }
}
