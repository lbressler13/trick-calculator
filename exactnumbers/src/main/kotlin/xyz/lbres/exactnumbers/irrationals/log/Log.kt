package xyz.lbres.exactnumbers.irrationals.log

import xyz.lbres.common.divideBigDecimals
import xyz.lbres.common.divideByZero
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.common.Irrational
import xyz.lbres.exactnumbers.irrationals.common.div
import xyz.lbres.exactnumbers.irrationals.common.times
import xyz.lbres.exactnumbers.irrationals.pi.Pi
import xyz.lbres.exactnumbers.irrationals.sqrt.Sqrt
import xyz.lbres.expressions.term.Term
import xyz.lbres.kotlinutils.biginteger.ext.isZero
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Representation of a log, with an integer base and rational argument
 *
 * @param argument [ExactFraction]: value to compute log of
 * @param base [Int]: base to use when computing log
 * @param isDivided [Boolean]: if the inverse of the value should be calculated
 * @param fullySimplified [Boolean]: if the value has already been simplified, such that getSimplified will return the same value
 * @throws [ArithmeticException] if number is not positive, base is less than 1, or value is 0 when isDivided is true
 */
class Log private constructor(
    val argument: ExactFraction,
    val base: Int,
    override val isDivided: Boolean,
    private val fullySimplified: Boolean
) : Comparable<Log>, Irrational {
    override val type = TYPE

    init {
        when {
            argument == ExactFraction.ONE && isDivided -> throw divideByZero
            argument.isZero() -> throw ArithmeticException("Cannot calculate log of 0")
            argument.isNegative() -> throw ArithmeticException("Cannot calculate log of negative number")
            base <= 1 -> throw ArithmeticException("Log base must be greater than 1")
        }
    }

    // constructors with reduced params + other types
    constructor(argument: ExactFraction) : this(argument, base = 10, isDivided = false, false)
    constructor(argument: ExactFraction, base: Int) : this(argument, base, isDivided = false, false)
    constructor(argument: ExactFraction, isDivided: Boolean) : this(argument, 10, isDivided, false)
    constructor(argument: ExactFraction, base: Int, isDivided: Boolean) : this(argument, base, isDivided, false)

    constructor(argument: Int) : this(ExactFraction(argument))
    constructor(argument: Int, base: Int) : this(ExactFraction(argument), base)
    constructor(argument: Int, isDivided: Boolean) : this(ExactFraction(argument), isDivided)
    constructor(argument: Int, base: Int, isDivided: Boolean) : this(ExactFraction(argument), base, isDivided)
    constructor(argument: Long) : this(ExactFraction(argument))
    constructor(argument: Long, base: Int) : this(ExactFraction(argument), base)
    constructor(argument: Long, isDivided: Boolean) : this(ExactFraction(argument), isDivided)
    constructor(argument: Long, base: Int, isDivided: Boolean) : this(ExactFraction(argument), base, isDivided)
    constructor(argument: BigInteger) : this(ExactFraction(argument))
    constructor(argument: BigInteger, base: Int) : this(ExactFraction(argument), base)
    constructor(argument: BigInteger, isDivided: Boolean) : this(ExactFraction(argument), isDivided)
    constructor(argument: BigInteger, base: Int, isDivided: Boolean) : this(ExactFraction(argument), base, isDivided)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Log) {
            return false
        }

        return getValue() == other.getValue()
    }

    // public methods to expose general Irrational operators
    operator fun times(other: Log): Term = times(other as Irrational)
    operator fun times(other: Pi): Term = times(other as Irrational)
    operator fun times(other: Sqrt): Term = times(other as Irrational)
    operator fun div(other: Log): Term = div(other as Irrational)
    operator fun div(other: Pi): Term = div(other as Irrational)
    operator fun div(other: Sqrt): Term = div(other as Irrational)

    override operator fun compareTo(other: Log): Int = getValue().compareTo(other.getValue())

    override fun isZero(): Boolean = argument == ExactFraction.ONE

    /**
     * Determine if the value of the log is a rational number.
     *
     * @return [Boolean]: true if the value is rational, false otherwise
     */
    override fun isRational(): Boolean {
        val numLog = getLogOf(argument.numerator, base)
        val denomLog = getLogOf(argument.denominator, base)

        // rational if both values are whole numbers
        return numLog.toPlainString().indexOf('.') == -1 && denomLog.toPlainString().indexOf('.') == -1
    }

    /**
     * Get the value of the log as a rational value if rational
     *
     * @return [ExactFraction?]: value of the log, or null if the value is irrational
     */
    override fun getRationalValue(): ExactFraction? {
        if (!isRational()) {
            return null
        }

        if (isZero()) {
            return ExactFraction.ZERO
        }

        val numLog = getLogOf(argument.numerator, base).toBigInteger()
        val denomLog = getLogOf(argument.denominator, base).toBigInteger()

        val result = when {
            numLog.isZero() -> -ExactFraction(denomLog) // numerator of argument is 1
            denomLog.isZero() -> ExactFraction(numLog) // denominator of argument is 1
            else -> ExactFraction(numLog, denomLog)
        }

        return if (isDivided) {
            result.inverse()
        } else {
            result
        }
    }

    /**
     * Get value of log, using the expression log_b(x/y) = log_b(x) - log_b(y).
     * This reduces loss of precision when casting to Double.
     *
     * @return [BigDecimal]
     */
    override fun getValue(): BigDecimal {
        val logValue = getLogOf(argument.numerator, base) - getLogOf(argument.denominator, base)

        if (!isDivided) {
            return logValue
        }

        return divideBigDecimals(BigDecimal.ONE, logValue)
    }

    /**
     * Simplify log into a coefficient and a log value.
     * Extracts rational value as coefficient and returns log as 1, or returns coefficient as 1 with the existing log for irrational logs.
     *
     * @return [Pair<ExactFraction, Log>]: a pair of coefficient and log such that the product has the same value as the current log
     */
    // TODO: improve the process of simplifying
    fun getSimplified(): Pair<ExactFraction, Log> {
        when {
            fullySimplified -> return Pair(ExactFraction.ONE, this)
            isZero() -> return Pair(ExactFraction.ONE, ZERO)
            equals(ONE) -> return Pair(ExactFraction.ONE, ONE)
        }

        val rational = getRationalValue()
        if (rational == null) {
            return Pair(ExactFraction.ONE, Log(argument, base, isDivided, true))
        }

        return Pair(rational, ONE)
    }

    override fun swapDivided(): Log {
        if (isZero()) {
            throw divideByZero
        }

        return Log(argument, base, !isDivided)
    }

    override fun toString(): String {
        val numString = if (argument.denominator == BigInteger.ONE) {
            argument.numerator.toString()
        } else {
            "${argument.numerator}/${argument.denominator}"
        }

        if (isDivided) {
            return "[1/log_$base($numString)]"
        }

        return "[log_$base($numString)]"
    }

    override fun hashCode(): Int = listOf(TYPE, argument, base, isDivided).hashCode()

    companion object {
        const val TYPE = "log"

        val ZERO = Log(ExactFraction.ONE, 10, isDivided = false, fullySimplified = true)
        val ONE = Log(ExactFraction.TEN, 10, isDivided = false, fullySimplified = true)

        /**
         * Extract rational values and simplify remaining list of irrationals
         *
         * @param numbers [List<Irrational>]: list to simplify, expected to consist of only Logs
         * @return [Pair<ExactFraction, List<Log>>]: product of rational values and simplified list of irrational values
         * @throws [ClassCastException] if any of the numbers are not a Log
         */
        // TODO: improve simplification by looking at bases
        internal fun simplifyList(numbers: List<Irrational>?): Pair<ExactFraction, List<Log>> {
            if (numbers.isNullOrEmpty()) {
                return Pair(ExactFraction.ONE, listOf())
            }

            @Suppress("UNCHECKED_CAST")
            numbers as List<Log>

            if (numbers.any(Log::isZero)) {
                return Pair(ExactFraction.ZERO, listOf())
            }

            val simplifiedNums = numbers.map { it.getSimplified() }
            val coeff = simplifiedNums.fold(ExactFraction.ONE) { acc, pair -> acc * pair.first }

            val combinedNums: List<Log> = simplifiedNums.map { it.second }
                .groupBy { Pair(it.argument, it.base) }
                .flatMap { pair ->
                    if (Log(pair.key.first, pair.key.second) == ONE) {
                        listOf()
                    } else {
                        val currentLogs = pair.value
                        val countDivided = currentLogs.count { it.isDivided }
                        val countNotDivided = currentLogs.size - countDivided

                        when {
                            countDivided == countNotDivided -> listOf()
                            countDivided > countNotDivided -> List(countDivided - countNotDivided) {
                                Log(pair.key.first, pair.key.second, isDivided = true, fullySimplified = true)
                            }
                            else -> List(countNotDivided - countDivided) {
                                Log(
                                    pair.key.first,
                                    pair.key.second,
                                    isDivided = false,
                                    fullySimplified = true
                                )
                            }
                        }
                    }
                }

            return Pair(coeff, combinedNums)
        }
    }
}
