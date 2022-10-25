package xyz.lbres.trickcalculator.ui.settings

import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import xyz.lbres.trickcalculator.R

/**
 * [ViewAssertion] to assert that settings displayed in settings fragment do not match initial settings
 */
private class SettingsModifiedViewAssertion : ViewAssertion {
    /**
     * Assert that a the settings do not match the initial settings
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
                view.findViewById<SwitchCompat>(R.id.clearOnErrorSwitch).isChecked,
                historyRandomness,
                view.findViewById<SwitchCompat>(R.id.settingsButtonSwitch).isChecked,
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
 * Wrapper function to create a [SettingsModifiedViewAssertion]
 */
fun settingsRandomized(): ViewAssertion = SettingsModifiedViewAssertion()
