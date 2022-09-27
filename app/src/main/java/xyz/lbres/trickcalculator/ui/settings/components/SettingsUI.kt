package xyz.lbres.trickcalculator.ui.settings.components

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat

/**
 * Interface with values that must be present in a fragment that contains a menu to modify settings
 */
interface SettingsUI {
    /**
     * Flags for events in fragment
     */
    var resetPressed: Boolean
    var randomizePressed: Boolean

    /**
     * UI elements to represent settings
     */
    // switches
    var applyDecimalsSwitch: SwitchCompat
    var applyParensSwitch: SwitchCompat
    var clearOnErrorSwitch: SwitchCompat
    var settingsButtonSwitch: SwitchCompat
    var shuffleComputationSwitch: SwitchCompat
    var shuffleNumbersSwitch: SwitchCompat
    var shuffleOperatorsSwitch: SwitchCompat

    // radio group
    var historyRadioGroup: RadioGroup
    var historyRadioButtons: List<RadioButton>

    // all settings buttons
    var randomizeSettingsButton: View
    var resetSettingsButton: View
}
