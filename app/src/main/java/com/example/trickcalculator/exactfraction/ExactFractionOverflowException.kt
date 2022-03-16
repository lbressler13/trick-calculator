package com.example.trickcalculator.exactfraction

/**
<<<<<<< HEAD:app/src/main/java/com/example/trickcalculator/exactfraction/ExactFractionOverflowException.kt
 * ArithmeticException specifically for ExactFraction casting overflow.
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
