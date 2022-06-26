package com.example.trickcalculator.ui.settings

data class Settings(
    var applyDecimals: Boolean,
    var applyParens: Boolean,
    var clearOnError: Boolean,
    var historyRandomness: Int,
    var showSettingsButton: Boolean,
    var shuffleComputation: Boolean,
    var shuffleNumbers: Boolean,
    var shuffleOperators: Boolean,
) {
    constructor() : this(
        applyDecimals = true,
        applyParens = true,
        clearOnError = false,
        historyRandomness = 1,
        showSettingsButton = false,
        shuffleComputation = false,
        shuffleNumbers = false,
        shuffleOperators = true
    )
}
