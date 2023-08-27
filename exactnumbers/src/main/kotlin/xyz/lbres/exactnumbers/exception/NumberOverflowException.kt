package xyz.lbres.exactnumbers.exception

/**
 * [ArithmeticException] for number overflow
 */
class NumberOverflowException(override val message: String?) : ArithmeticException(message) {
    constructor() : this(null)
}
