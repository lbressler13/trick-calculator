package com.example.trickcalculator.ui.history

import exactfraction.ExactFraction
import exactfraction.checkIsEFString
import com.example.trickcalculator.ext.copyWithReplacement
import com.example.trickcalculator.utils.StringList

class HistoryItem {
    val computation: String
    val result: ExactFraction?
    val error: String?

    constructor(computation: String, result: ExactFraction) {
        this.computation = computation
        this.result = result
        this.error = null
    }

    constructor(computation: String, error: String) {
        this.computation = computation
        this.result = null
        this.error = error
    }

    constructor(computation: StringList, error: String) :
            this(computation.joinToString(""), error)

    constructor(computation: StringList, result: ExactFraction) {
        // parse EF-formatted value into decimal string
        if (computation.isNotEmpty() && checkIsEFString(computation[0])) {
            val decimal = ExactFraction(computation[0]).toDecimalString(5)
            val newComputation = computation.copyWithReplacement(0, decimal)
            this.computation = newComputation.joinToString("")
        } else {
            this.computation = computation.joinToString("")
        }
        this.result = result
        this.error = null
    }

    override fun toString(): String {
        if (error != null) {
            return "HI[$computation, err: $error]"
        }

        return "HI[$computation, res: $result]"
    }
}