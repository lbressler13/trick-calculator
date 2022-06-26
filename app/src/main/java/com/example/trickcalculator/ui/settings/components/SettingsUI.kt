package com.example.trickcalculator.ui.settings.components

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment

/**
 * Interface with values that must be present in both SettingsFragment and SettingsDialog
 */
interface SettingsUI {
    // implementing fragment
    val fragment: Fragment

    // flags for reset and randomize being pressed
    var resetPressed: Boolean
    var randomizePressed: Boolean

    // UI elements related to settings
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
