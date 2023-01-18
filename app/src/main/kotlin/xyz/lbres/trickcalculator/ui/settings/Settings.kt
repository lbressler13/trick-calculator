package xyz.lbres.trickcalculator.ui.settings

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
        shuffleOperators = true
    )

    /**
     * Function defining tow [Settings] objects as equal when all settings are the same
     */
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Settings) {
            return false
        }

        return applyDecimals == other.applyDecimals &&
            applyParens == other.applyParens &&
            clearOnError == other.clearOnError &&
            historyRandomness == other.historyRandomness &&
            showSettingsButton == other.showSettingsButton &&
            shuffleComputation == other.shuffleComputation &&
            shuffleNumbers == other.shuffleNumbers &&
            shuffleOperators == other.shuffleOperators
    }
}
