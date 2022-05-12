package com.example.trickcalculator.ui.history

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.exactfraction.checkIsEFString
import com.example.trickcalculator.ext.copyWithReplacement
import com.example.trickcalculator.utils.StringList

class HistoryItem {
    val computation: String
    val result: ExactFraction?
    val error: String?

    constructor(computation: StringList, error: String) {
        this.computation = computation.joinToString("")
        this.result = null
        this.error = error
    }

    constructor(computation: StringList, result: ExactFraction) {
        if (computation.isNotEmpty() && checkIsEFString(computation[0])) {
            val parsed = ExactFraction(computation[0]).toDecimalString(5)
            val newComputation = computation.copyWithReplacement(0, parsed)
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