package xyz.lbres.trickcalculator.ui.settings

// TODO alphabetize params

/**
 * Object containing all settings
 *
 * @param applyDecimals [Boolean]
 * @param applyParens [Boolean]
 * @param clearOnError [Boolean]
 * @param historyRandomness [Int]
 * @param showSettingsButton [Boolean]
 * @param shuffleComputation [Boolean]
 * @param shuffleNumbers [Boolean]
 * @param shuffleOperators [Boolean]
 */
data class Settings(
    var applyDecimals: Boolean,
    var applyParens: Boolean,
    var clearOnError: Boolean,
    var historyRandomness: Int,
    var showSettingsButton: Boolean,
    var shuffleComputation: Boolean,
    var shuffleNumbers: Boolean,
    var shuffleOperators: Boolean,
    var randomizeSigns: Boolean
) {
    /**
     * Constructor with default values
     */
    constructor() : this(
        applyDecimals = true,
        applyParens = true,
        clearOnError = false,
        historyRandomness = 1,
        showSettingsButton = false,
        shuffleComputation = false,
        shuffleNumbers = false,
        shuffleOperators = true,
        randomizeSigns = false
    )
}
