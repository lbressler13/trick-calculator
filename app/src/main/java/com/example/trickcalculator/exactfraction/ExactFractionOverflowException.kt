package com.example.trickcalculator.exactfraction

/**
 * ArithmeticException specifically for exact fraction casting overflow.
 * Has specific field for value of string that caused overflow
 */
class ExactFractionOverflowException() : ArithmeticException() {
    override var message: String? = null
    var overflowValue: String? = null

    constructor (message: String) : this() {
        this.message = message
    }

    constructor (message: String, overflowValue: String) : this(message) {
        this.overflowValue = overflowValue
    }
}
