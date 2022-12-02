package xyz.lbres.trickcalculator.ui.settings

import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import xyz.lbres.trickcalculator.R

/**
 * [ViewAssertion] to assert that settings displayed in settings fragment have been randomized
 */
private class SettingsRandomizedViewAssertion : ViewAssertion {
    /**
     * Assert that the settings have been randomized
     *
     * @param view [View]?: view to check, expected to be root of a settings fragment
     */
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (view == null) {
            throw AssertionError("Settings view is null")
        } else {
            val initialSettings = Settings()

            val historyGroup = view.findViewById<RadioGroup>(R.id.historyRandomnessGroup)
            val historyRandomness = when (historyGroup.checkedRadioButtonId) {
                R.id.historyButton0 -> 0
                R.id.historyButton1 -> 1
                R.id.historyButton2 -> 2
                R.id.historyButton3 -> 3
                else -> initialSettings.historyRandomness
            }

            val viewSettings = Settings(
                view.findViewById<SwitchCompat>(R.id.applyDecimalsSwitch).isChecked,
                view.findViewById<SwitchCompat>(R.id.applyParensSwitch).isChecked,
                initialSettings.clearOnError, // hardcoded in randomize, not part of randomization
                historyRandomness,
                initialSettings.showSettingsButton, // hardcoded in randomize, not part of randomization
                view.findViewById<SwitchCompat>(R.id.shuffleComputationSwitch).isChecked,
                view.findViewById<SwitchCompat>(R.id.shuffleNumbersSwitch).isChecked,
                view.findViewById<SwitchCompat>(R.id.shuffleOperatorsSwitch).isChecked,
            )

            if (viewSettings == initialSettings) {
                throw AssertionError("Randomized settings match initial settings")
            }
        }
    }
}

/**
 * Wrapper function to create a [SettingsRandomizedViewAssertion]
 */
fun settingsRandomized(): ViewAssertion = SettingsRandomizedViewAssertion()
