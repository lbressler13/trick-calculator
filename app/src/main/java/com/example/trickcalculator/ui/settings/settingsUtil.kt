package com.example.trickcalculator.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.DialogSettingsBinding
import com.example.trickcalculator.databinding.FragmentSettingsBinding
import com.example.trickcalculator.ui.shared.SharedViewModel

/**
 * The settings package contains a [SettingsFragment] and [SettingsDialog], which contain similar UI elements and expose the same settings.
 * Changes to the available settings will require changes in both the fragment and dialog.
 * To keep the fragments in sync, the majority of the code is pushed into util functions.
 * This ensures that any UI or logic changes must happen in both the dialog and fragment.
 */

/**
 * Check or uncheck a switch based on arguments
 *
 * @param context [Context]: application context
 * @param args: [Bundle]: nullable field containing arguments of several types
 * @param keyId [Int]: resourceId of key to access specified argument
 * @param switch [SwitchCompat]: switch to check
 */
fun setSwitchUiFromArgs(context: Context, args: Bundle?, keyId: Int, switch: SwitchCompat) {
    val key: String = context.getString(keyId)
    val value: Boolean? = args?.getBoolean(key)
    if (value != null) {
        switch.isChecked = value
    }
}

/**
 * Get history randomness value from radio group
 *
 * @param group [RadioGroup]
 * @param buttons [List]: list of [RadioButton] within group
 * @return [Int] the index of the checked button, corresponding to the history randomness
 */
fun getHistoryGroupValue(group: RadioGroup, buttons: List<RadioButton>): Int {
    val index = buttons.indexOfFirst { it.id == group.checkedRadioButtonId }
    if (index >= 0) {
        return index
    }

    return 0
}

/**
 * Set the checked button in the history radio group based on args
 *
 * @param context [Context]: application context
 * @param args [Bundle]: nullable field containing arguments of several types
 * @param group [RadioGroup]
 * @param buttons [List]: list of [RadioButton] within group
 */
fun setHistoryRadioGroup(context: Context, args: Bundle?, group: RadioGroup, buttons: List<RadioButton>) {
    val key: String = context.getString(R.string.key_random_history)
    val value: Int = args?.getInt(key) ?: 0
    group.check(buttons[value].id)
}

/**
 * Initialize the UI binding based on information in an args object.
 * Identifies binding as belonging to a [SettingsFragment] or [SettingsDialog], and identifies UI elements accordingly.
 *
 * @param fragment [Fragment]: calling fragment.
 * Expected to be either [SettingsFragment] or [SettingsDialog]
 * @param viewModel [SharedViewModel]: ViewModel containing fields for all settings
 * @param binding [ViewBinding]: binding of the calling fragment.
 * Expected to be either [FragmentSettingsBinding] or [DialogSettingsBinding]
 */
fun setUiFromArgs(fragment: Fragment, viewModel: SharedViewModel, binding: ViewBinding) {
    val context = fragment.requireContext()
    val args = fragment.arguments

    // create variables for UI elements
    val shuffleNumbersSwitch: SwitchCompat
    val shuffleOperatorsSwitch: SwitchCompat
    val applyParensSwitch: SwitchCompat
    val clearOnErrorSwitch: SwitchCompat
    val applyDecimalsSwitch: SwitchCompat
    val settingsButtonSwitch: SwitchCompat
    val historyRadioGroup: RadioGroup
    val historyRadioButtons: List<RadioButton>
    val resetSettingsButton: View
    val randomizeSettingsButton: View

    // assign elements for SettingsFragment
    if (binding is FragmentSettingsBinding) {
        shuffleNumbersSwitch = binding.shuffleNumbersSwitch
        shuffleOperatorsSwitch = binding.shuffleOperatorsSwitch
        applyParensSwitch = binding.applyParensSwitch
        clearOnErrorSwitch = binding.clearOnErrorSwitch
        applyDecimalsSwitch = binding.applyDecimalsSwitch
        settingsButtonSwitch = binding.settingsButtonSwitch
        historyRadioGroup = binding.historyRandomnessGroup
        historyRadioButtons = listOf(
            binding.historyButton0,
            binding.historyButton1,
            binding.historyButton2,
            binding.historyButton3
        )
        resetSettingsButton = binding.resetSettingsButton
        randomizeSettingsButton = binding.randomizeSettingsButton
    }
    // assign elements for SettingsDialog
    else {
        binding as DialogSettingsBinding
        shuffleNumbersSwitch = binding.shuffleNumbersSwitch
        shuffleOperatorsSwitch = binding.shuffleOperatorsSwitch
        applyParensSwitch = binding.applyParensSwitch
        clearOnErrorSwitch = binding.clearOnErrorSwitch
        applyDecimalsSwitch = binding.applyDecimalsSwitch
        settingsButtonSwitch = binding.settingsButtonSwitch
        historyRadioGroup = binding.historyRandomnessGroup
        historyRadioButtons = listOf(
            binding.historyButton0,
            binding.historyButton1,
            binding.historyButton2,
            binding.historyButton3
        )
        resetSettingsButton = binding.resetSettingsButton
        randomizeSettingsButton = binding.randomizeSettingsButton
    }

    // set UI, for either fragment or dialog
    setSwitchUiFromArgs(context, args, R.string.key_shuffle_numbers, shuffleNumbersSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_shuffle_operators, shuffleOperatorsSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_apply_parens, applyParensSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_clear_on_error, clearOnErrorSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_apply_decimals, applyDecimalsSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_settings_button, settingsButtonSwitch)

    val mainFragmentKey = context.getString(R.string.key_main_fragment)
    val isMainFragment = args?.getBoolean(mainFragmentKey)
    settingsButtonSwitch.isVisible = isMainFragment != true

    setHistoryRadioGroup(context, args, historyRadioGroup, historyRadioButtons)
    resetSettingsButton.setOnClickListener { resetSettingsOnClick(fragment) }
    randomizeSettingsButton.setOnClickListener { randomizeSettingsOnClick(fragment) }
}

/**
 * Update ViewModel using values selected in UI.
 * Should be called when at the end of the lifecycle of the calling fragment.
 *
 * @param fragment [Fragment]: calling fragment.
 * Expected to be either [SettingsFragment] or [SettingsDialog]
 * @param viewModel [SharedViewModel]: ViewModel containing fields for all settings
 * @param binding [ViewBinding]: binding of the calling fragment.
 * Expected to be either [FragmentSettingsBinding] or [DialogSettingsBinding]
 */
fun saveToViewModel(fragment: Fragment, viewModel: SharedViewModel, binding: ViewBinding) {
    val resetRandomizedPair = getResetAndRandomized(fragment)
    val resetPressed = resetRandomizedPair.first
    val randomizedPressed = resetRandomizedPair.second

    when {
        resetPressed -> viewModel.resetSettings()
        randomizedPressed -> viewModel.randomizeSettings()
        else -> {
            // create variables for UI elements
            val shuffleNumbersSwitch: SwitchCompat
            val shuffleOperatorsSwitch: SwitchCompat
            val applyParensSwitch: SwitchCompat
            val clearOnErrorSwitch: SwitchCompat
            val applyDecimalsSwitch: SwitchCompat
            val settingsButtonSwitch: SwitchCompat
            val historyRadioGroup: RadioGroup
            val historyRadioButtons: List<RadioButton>

            // assign elements for SettingsFragment
            if (binding is FragmentSettingsBinding) {
                shuffleNumbersSwitch = binding.shuffleNumbersSwitch
                shuffleOperatorsSwitch = binding.shuffleOperatorsSwitch
                applyParensSwitch = binding.applyParensSwitch
                clearOnErrorSwitch = binding.clearOnErrorSwitch
                applyDecimalsSwitch = binding.applyDecimalsSwitch
                settingsButtonSwitch = binding.settingsButtonSwitch
                historyRadioGroup = binding.historyRandomnessGroup
                historyRadioButtons = listOf(
                    binding.historyButton0,
                    binding.historyButton1,
                    binding.historyButton2,
                    binding.historyButton3
                )
            }
            // assign elements for SettingsDialog
            else {
                binding as DialogSettingsBinding
                shuffleNumbersSwitch = binding.shuffleNumbersSwitch
                shuffleOperatorsSwitch = binding.shuffleOperatorsSwitch
                applyParensSwitch = binding.applyParensSwitch
                clearOnErrorSwitch = binding.clearOnErrorSwitch
                applyDecimalsSwitch = binding.applyDecimalsSwitch
                settingsButtonSwitch = binding.settingsButtonSwitch
                historyRadioGroup = binding.historyRandomnessGroup
                historyRadioButtons = listOf(
                    binding.historyButton0,
                    binding.historyButton1,
                    binding.historyButton2,
                    binding.historyButton3
                )
            }

            // update ViewModel based on settings selected in UI, for either fragment or dialog
            viewModel.setShuffleNumbers(shuffleNumbersSwitch.isChecked)
            viewModel.setShuffleOperators(shuffleOperatorsSwitch.isChecked)
            viewModel.setApplyParens(applyParensSwitch.isChecked)
            viewModel.setClearOnError(clearOnErrorSwitch.isChecked)
            viewModel.setApplyDecimals(applyDecimalsSwitch.isChecked)
            viewModel.setShowSettingsButton(settingsButtonSwitch.isChecked)
            viewModel.setHistoryRandomness(getHistoryGroupValue(historyRadioGroup, historyRadioButtons))
        }
    }

    // reset button pressed
    if (fragment is SettingsDialog) {
        fragment.resetPressed = false
        fragment.randomizePressed = false
    } else {
        fragment as SettingsFragment
        fragment.resetPressed = false
        fragment.randomizePressed = false
    }
}

/**
 * Get values of resetPressed and randomizePressed, based on fragment
 *
 * @param fragment [Fragment]: calling fragment.
 * Expected to be either [SettingsFragment] or [SettingsDialog]
 * @return [Pair]: pair of [Boolean], where first element is resetPressed and second is randomizedPressed
 */
private fun getResetAndRandomized(fragment: Fragment): Pair<Boolean, Boolean> {
    val resetPressed: Boolean
    val randomizedPressed: Boolean

    if (fragment is SettingsDialog) {
        resetPressed = fragment.resetPressed
        randomizedPressed = fragment.randomizePressed
    } else {
        fragment as SettingsFragment
        resetPressed = fragment.resetPressed
        randomizedPressed = fragment.randomizePressed
    }

    return Pair(resetPressed, randomizedPressed)
}

/**
 * On click function for reset settings button.
 *
 * @param fragment [Fragment]: the calling fragment.
 * Expected to be either [SettingsFragment] or [SettingsDialog]
 */
private fun resetSettingsOnClick(fragment: Fragment) {
    if (fragment is SettingsDialog)  {
        fragment.resetPressed = true
        fragment.dismiss()
    } else if (fragment is SettingsFragment) {
        fragment.resetPressed = true
        fragment.parentFragmentManager.popBackStack()
    }
}

/**
 * On click function for randomize settings button.
 *
 * @param fragment [Fragment]: the calling fragment.
 * Expected to be either [SettingsFragment] or [SettingsDialog]
 */
private fun randomizeSettingsOnClick(fragment: Fragment) {
    if (fragment is SettingsDialog)  {
        fragment.randomizePressed = true
        fragment.dismiss()
    } else if (fragment is SettingsFragment) {
        fragment.randomizePressed = true
        fragment.parentFragmentManager.popBackStack()
    }
}
