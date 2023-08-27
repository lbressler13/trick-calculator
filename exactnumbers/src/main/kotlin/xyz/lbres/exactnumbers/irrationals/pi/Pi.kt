package xyz.lbres.exactnumbers.irrationals.pi

import xyz.lbres.exactnumbers.common.divideBigDecimals
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.expressions.term.Term
import xyz.lbres.exactnumbers.irrationals.common.Irrational
import xyz.lbres.exactnumbers.irrationals.common.div
import xyz.lbres.exactnumbers.irrationals.common.times
import xyz.lbres.exactnumbers.irrationals.log.Log
import xyz.lbres.exactnumbers.irrationals.sqrt.Sqrt
import xyz.lbres.kotlinutils.general.simpleIf
import java.math.BigDecimal
import kotlin.math.PI
import kotlin.math.abs

/**
 * Representation of pi
 *
 * @param isDivided [Boolean]: if the inverse of the value should be calculated
 */
class Pi(override val isDivided: Boolean) : Irrational {
    override val type: String = TYPE

    // constructor with reduced params
    constructor() : this(false)

    override fun getValue(): BigDecimal {
        val base = PI.toBigDecimal()

        if (isDivided) {
            return divideBigDecimals(BigDecimal.ONE, base)
        }

        return base
    }

    override fun isZero(): Boolean = false

    override fun isRational(): Boolean = false

    override fun getRationalValue(): ExactFraction? = null

    override fun swapDivided(): Pi = Pi(!isDivided)

    override fun equals(other: Any?): Boolean {
        return other != null && other is Pi && isDivided == other.isDivided
    }

    // public methods to expose general Irrational operators
    operator fun times(other: Log): Term = times(other as Irrational)
    operator fun times(other: Pi): Term = times(other as Irrational)
    operator fun times(other: Sqrt): Term = times(other as Irrational)
    operator fun div(other: Log): Term = div(other as Irrational)
    operator fun div(other: Pi): Term = div(other as Irrational)
    operator fun div(other: Sqrt): Term = div(other as Irrational)

    override fun toString(): String = simpleIf(isDivided, "[1/π]", "[π]")

    override fun hashCode(): Int = listOf(TYPE, PI, isDivided).hashCode()

    companion object {
        const val TYPE = "pi"

        /**
         * Simplify list of pis
         *
         * @param numbers [List<Irrational>] : list to simplify, expected to consist of only Pis
         * @return [List<Pi>]: simplified list
         * @throws [ClassCastException] if any of the numbers are not a Pi
         */
        internal fun simplifyList(numbers: List<Irrational>?): List<Pi> {
            if (numbers.isNullOrEmpty()) {
                return emptyList()
            }

            @Suppress("UNCHECKED_CAST")
            numbers as List<Pi>

            val positive = numbers.count { !it.isDivided }
            val negative = numbers.size - positive
            val diff = abs(positive - negative)

            return List(diff) { Pi(isDivided = positive < negative )}
        }
    }
}
