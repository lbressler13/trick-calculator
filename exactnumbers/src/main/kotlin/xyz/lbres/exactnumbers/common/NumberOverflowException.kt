package xyz.lbres.exactnumbers.common

// TODO find correct package

/**
 * [ArithmeticException] for number overflow
 */
class NumberOverflowException(override val message: String?) : ArithmeticException(message) {
    constructor() : this(null)
}
