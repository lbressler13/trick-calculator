package xyz.lbres.trickcalculator.ui.settings

import androidx.lifecycle.ViewModel
import java.util.Date
import kotlin.random.Random

/**
 * ViewModel to track settings that are shared across fragments
 */
class SettingsViewModel : ViewModel() {
    private val random = Random(Date().time)

    /**
     * Individual settings
     */
    var applyDecimals: Boolean = true
    var applyParens: Boolean = true
    var clearOnError: Boolean = false
    var historyRandomness: Int = 1
    var showSettingsButton: Boolean = false
    var shuffleComputation: Boolean = false
    var shuffleNumbers: Boolean = false
    var shuffleOperators: Boolean = true

    /**
     * All settings
     */

    /**
     * Reset all settings other than displaying settings button on calculator fragment
     */
    fun resetSettings() {
        val defaults = Settings()
        applyDecimals = defaults.applyDecimals
        applyParens = defaults.applyParens
        clearOnError = defaults.clearOnError
        historyRandomness = defaults.historyRandomness
        shuffleComputation = defaults.shuffleComputation
        shuffleNumbers = defaults.shuffleNumbers
        shuffleOperators = defaults.shuffleOperators
    }

    /**
     * Select random settings, clear on error, and hide settings button on calculator fragment
     */
    fun randomizeSettings() {
        applyDecimals = random.nextBoolean()
        applyParens = random.nextBoolean()
        shuffleComputation = random.nextBoolean()
        shuffleNumbers = random.nextBoolean()
        shuffleOperators = random.nextBoolean()

        historyRandomness = (0..3).random(random)

        clearOnError = true
        showSettingsButton = false
    }

    /**
     * Set the calculator to use behave as a normal calculator. Does not affect settings button.
     */
    fun setStandardSettings() {
        applyDecimals = true
        applyParens = true
        clearOnError = false
        historyRandomness = 0
        shuffleComputation = false
        shuffleNumbers = false
        shuffleOperators = false
    }
}
