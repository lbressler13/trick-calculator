package com.example.trickcalculator.ui.settings

data class Settings(
    var shuffleNumbers: Boolean,
    var shuffleOperators: Boolean,
    var applyParens: Boolean,
    var clearOnError: Boolean,
    var applyDecimals: Boolean,
    var showSettingsButton: Boolean,
    var historyRandomness: Int,
    var shuffleComputation: Boolean
) {
    constructor() : this(
        shuffleNumbers = false,
        shuffleOperators = true,
        applyParens = true,
        clearOnError = false,
        applyDecimals = true,
        showSettingsButton = false,
        historyRandomness = 1,
        shuffleComputation = false
    )
}
