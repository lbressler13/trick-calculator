package com.example.trickcalculator.bigfraction

/**
 * ArithmeticException specifically for big fraction casting overflow.
 * Has specific field for value of string that caused overflow
 */
class BigFractionOverflowException() : ArithmeticException() {
    override var message: String? = null
    var overflowValue: String? = null

    constructor (message: String) : this() {
        this.message = message
    }

    constructor (message: String, overflowValue: String) : this(message) {
        this.overflowValue = overflowValue
    }
}
