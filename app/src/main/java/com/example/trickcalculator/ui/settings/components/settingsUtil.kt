package com.example.trickcalculator.ui.settings.components

import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.trickcalculator.R
import com.example.trickcalculator.ui.shared.SharedViewModel

/**
 * The settings package contains a [SettingsFragment] and [SettingsDialog], which contain similar UI elements and expose the same settings.
 * Changes to the available settings will require changes in both the fragment and dialog.
 * To keep the fragments in sync, the majority of the code is pushed into util functions.
 * This ensures that any UI or logic changes must happen in both the dialog and fragment.
 */

/**
 * Get history randomness value from radio group
 *
 * @param group [RadioGroup]
 * @param buttons [List]: list of [RadioButton] within group
 * @return [Int] the index of the checked button, corresponding to the history randomness
 */
fun getHistoryGroupValue(group: RadioGroup, buttons: List<RadioButton>): Int {
    val index = buttons.indexOfFirst { it.id == group.checkedRadioButtonId }
    if (index in buttons.indices) {
        return index
    }

    return 0
}

/**
 * Observe changes to settings in view model and update ui based on changes.
 * Without this, changes from SettingsDialog will not be saved if dialog is opened on top of fragment.
 * Will not cause a loop of updates because settings aren't saved until fragment is closed.
 *
 * @param settingsUi [SettingsUI]: UI of calling fragment
 * @param viewModel [SharedViewModel]: view model with settings fields
 * @param lifecycleOwner [LifecycleOwner]
 */
fun initObservers(settingsUi: SettingsUI, viewModel: SharedViewModel, lifecycleOwner: LifecycleOwner) {
    viewModel.applyDecimals.observe(lifecycleOwner) { settingsUi.applyDecimalsSwitch.isChecked = it }
    viewModel.applyParens.observe(lifecycleOwner) { settingsUi.applyParensSwitch.isChecked = it }
    viewModel.clearOnError.observe(lifecycleOwner) { settingsUi.clearOnErrorSwitch.isChecked = it }
    viewModel.showSettingsButton.observe(lifecycleOwner) { settingsUi.settingsButtonSwitch.isChecked = it }
    viewModel.shuffleComputation.observe(lifecycleOwner) { settingsUi.shuffleComputationSwitch.isChecked = it }
    viewModel.shuffleNumbers.observe(lifecycleOwner) { settingsUi.shuffleNumbersSwitch.isChecked = it }
    viewModel.shuffleOperators.observe(lifecycleOwner) { settingsUi.shuffleOperatorsSwitch.isChecked = it }

    viewModel.historyRandomness.observe(lifecycleOwner) { settingsUi.historyRadioButtons[it].isChecked = true }
}

/**
 * Initialize the UI. Majority of items are set based on view model observers
 *
 * @param settingsUi [SettingsUI]: UI of calling fragment
 */
fun initUi(settingsUi: SettingsUI) {
    val context = settingsUi.fragment.requireContext()
    val args = settingsUi.fragment.arguments

    val mainFragmentKey = context.getString(R.string.key_main_fragment)
    val isMainFragment = args?.getBoolean(mainFragmentKey)
    settingsUi.settingsButtonSwitch.isVisible = isMainFragment != true

    settingsUi.randomizeSettingsButton.setOnClickListener { randomizeSettingsOnClick(settingsUi) }
    settingsUi.resetSettingsButton.setOnClickListener { resetSettingsOnClick(settingsUi) }
}

/**
 * Update ViewModel using values selected in UI.
 * Should be called when at the end of the lifecycle of the calling fragment.
 *
 * @param settingsUi [SettingsUI]: UI of calling fragment.
 * @param viewModel [SharedViewModel]: ViewModel containing fields for all settings
 */
fun saveToViewModel(settingsUi: SettingsUI, viewModel: SharedViewModel) {
    val randomizedPressed = settingsUi.randomizePressed
    val resetPressed = settingsUi.resetPressed

    when {
        randomizedPressed -> viewModel.randomizeSettings()
        resetPressed -> {
            // save changes to visibility of show settings
            viewModel.setShowSettingsButton(settingsUi.settingsButtonSwitch.isChecked)
            viewModel.resetSettings()
        }
        else -> {
            // update ViewModel based on settings selected in UI
            viewModel.setApplyDecimals(settingsUi.applyDecimalsSwitch.isChecked)
            viewModel.setApplyParens(settingsUi.applyParensSwitch.isChecked)
            viewModel.setClearOnError(settingsUi.clearOnErrorSwitch.isChecked)
            viewModel.setShowSettingsButton(settingsUi.settingsButtonSwitch.isChecked)
            viewModel.setShuffleComputation(settingsUi.shuffleComputationSwitch.isChecked)
            viewModel.setShuffleNumbers(settingsUi.shuffleNumbersSwitch.isChecked)
            viewModel.setShuffleOperators(settingsUi.shuffleOperatorsSwitch.isChecked)

            viewModel.setHistoryRandomness(
                getHistoryGroupValue(
                    settingsUi.historyRadioGroup,
                    settingsUi.historyRadioButtons
                )
            )
        }
    }

    settingsUi.randomizePressed = false
    settingsUi.resetPressed = false
}

/**
 * On click function for randomize settings button.
 *
 * @param settingsUi [SettingsUI]: UI and info about calling fragment
 */
private fun randomizeSettingsOnClick(settingsUi: SettingsUI) {
    settingsUi.randomizePressed = true
    closeCurrentFragment(settingsUi.fragment)
}

/**
 * On click function for reset settings button.
 *
 * @param settingsUi [SettingsUI]: UI and info about calling fragment
 */
private fun resetSettingsOnClick(settingsUi: SettingsUI) {
    settingsUi.resetPressed = true
    closeCurrentFragment(settingsUi.fragment)
}

/**
 * Close or dismiss the current fragment
 *
 * @param fragment [Fragment]: the current fragment.
 * Expected to be either [SettingsFragment] or [SettingsDialog]
 */
private fun closeCurrentFragment(fragment: Fragment) {
    if (fragment is DialogFragment) {
        fragment.dismiss()
    } else {
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
        if (currentFragment is DialogFragment && currentFragment.requireParentFragment() is DialogFragment) {
            (currentFragment.requireParentFragment() as DialogFragment).dismiss()
        } else if (currentFragment !is DialogFragment && currentFragment.parentFragmentManager.backStackEntryCount > 0) {
            currentFragment.parentFragmentManager.popBackStack()
        }
    } catch (e: Exception) {
        // expected to fail when ui is recreating due to configuration changes or via dev tools
        Log.e("Failed to close parent fragment:", e.message.toString())
    }
}
