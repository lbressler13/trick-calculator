package com.example.trickcalculator.ui.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.trickcalculator.R
import com.example.trickcalculator.ui.settings.components.SettingsDialog
import com.example.trickcalculator.ui.settings.components.SettingsFragment
import com.example.trickcalculator.ui.settings.components.SettingsUI
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
fun setHistoryRadioGroup(
    context: Context,
    args: Bundle?,
    group: RadioGroup,
    buttons: List<RadioButton>
) {
    val key: String = context.getString(R.string.key_random_history)
    val value: Int = args?.getInt(key) ?: 0
    group.check(buttons[value].id)
}

/**
 * Initialize the UI based on information in an args object.
 *
 * @param settingsUi [SettingsUI]: calling fragment.
 * Expected to be either [SettingsFragment] or [SettingsDialog]
 * @param viewModel [SharedViewModel]: ViewModel containing fields for all settings
 */
fun setUiFromArgs(settingsUi: SettingsUI, viewModel: SharedViewModel) {
    val context = settingsUi.fragment.requireContext()
    val args = settingsUi.fragment.arguments

    // set UI, for either fragment or dialog
    setSwitchUiFromArgs(context, args, R.string.key_shuffle_numbers, settingsUi.shuffleNumbersSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_shuffle_operators, settingsUi.shuffleOperatorsSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_apply_parens, settingsUi.applyParensSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_clear_on_error, settingsUi.clearOnErrorSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_apply_decimals, settingsUi.applyDecimalsSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_settings_button, settingsUi.settingsButtonSwitch)
    setSwitchUiFromArgs(context, args, R.string.key_shuffle_computation, settingsUi.shuffleComputationSwitch)

    val mainFragmentKey = context.getString(R.string.key_main_fragment)
    val isMainFragment = args?.getBoolean(mainFragmentKey)
    settingsUi.settingsButtonSwitch.isVisible = isMainFragment != true

    setHistoryRadioGroup(context, args, settingsUi.historyRadioGroup, settingsUi.historyRadioButtons)
    settingsUi.resetSettingsButton.setOnClickListener { resetSettingsOnClick(settingsUi.fragment) }
    settingsUi.randomizeSettingsButton.setOnClickListener { randomizeSettingsOnClick(settingsUi.fragment) }
}

/**
 * Update ViewModel using values selected in UI.
 * Should be called when at the end of the lifecycle of the calling fragment.
 *
 * @param fragment [Fragment]: calling fragment.
 * Expected to be either [SettingsFragment] or [SettingsDialog]
 * @param viewModel [SharedViewModel]: ViewModel containing fields for all settings
 */
fun saveToViewModel(settingUi: SettingsUI, viewModel: SharedViewModel) {
    val resetRandomizedPair = getResetAndRandomized(settingUi.fragment)
    val resetPressed = resetRandomizedPair.first
    val randomizedPressed = resetRandomizedPair.second

    when {
        resetPressed -> viewModel.resetSettings()
        randomizedPressed -> viewModel.randomizeSettings()
        else -> {
            // update ViewModel based on settings selected in UI, for either fragment or dialog
            viewModel.setShuffleNumbers(settingUi.shuffleNumbersSwitch.isChecked)
            viewModel.setShuffleOperators(settingUi.shuffleOperatorsSwitch.isChecked)
            viewModel.setApplyParens(settingUi.applyParensSwitch.isChecked)
            viewModel.setClearOnError(settingUi.clearOnErrorSwitch.isChecked)
            viewModel.setApplyDecimals(settingUi.applyDecimalsSwitch.isChecked)
            viewModel.setShuffleComputation(settingUi.shuffleComputationSwitch.isChecked)
            viewModel.setShowSettingsButton(settingUi.settingsButtonSwitch.isChecked)
            viewModel.setHistoryRandomness(
                getHistoryGroupValue(
                    settingUi.historyRadioGroup,
                    settingUi.historyRadioButtons
                )
            )
        }
    }

    settingUi.resetPressed = false
    settingUi.randomizePressed = false
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
    if (fragment is SettingsDialog) {
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
    if (fragment is SettingsDialog) {
        fragment.randomizePressed = true
        fragment.dismiss()
    } else if (fragment is SettingsFragment) {
        fragment.randomizePressed = true
        fragment.parentFragmentManager.popBackStack()
    }
}

/**
 * If the current fragment is a [SettingsDialog] and its parent is a dialog, closes the parent dialog.
 * If the current fragment is a [SettingsFragment], removes the previous fragment.
 *
 * @param currentFragment [Fragment]: the calling fragment.
 * Expected to be either [SettingsFragment] or [SettingsDialog]
 */
fun closePreviousFragment(currentFragment: Fragment) {
    try {
        if (currentFragment is SettingsDialog && currentFragment.requireParentFragment() is DialogFragment) {
            (currentFragment.requireParentFragment() as DialogFragment).dismiss()
        } else if (currentFragment is SettingsFragment && currentFragment.parentFragmentManager.backStackEntryCount > 0) {
            currentFragment.parentFragmentManager.popBackStack()
        }
    } catch (e: Exception) {
        // expected to fail after recreating ui through developer tools
        Log.e("Failed to close parent fragment:", e.message.toString())
    }
}
