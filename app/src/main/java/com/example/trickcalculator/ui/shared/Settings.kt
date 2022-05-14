package com.example.trickcalculator.ui.shared

data class Settings(
    var shuffleNumbers: Boolean,
    var shuffleOperators: Boolean,
    var applyParens: Boolean,
    var clearOnError: Boolean,
    var applyDecimals: Boolean,
    var showSettingsButton: Boolean
)
