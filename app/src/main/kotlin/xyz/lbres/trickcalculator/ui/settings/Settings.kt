package xyz.lbres.trickcalculator.ui.settings

/**
 * TODO
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
