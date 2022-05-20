package com.example.trickcalculator.ui.settings

import android.content.Context
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.DialogSettingsBinding
import com.example.trickcalculator.databinding.FragmentSettingsBinding
import com.example.trickcalculator.ui.shared.SharedViewModel


/**
 * Get information from fragment args to set if a switch should be checked
 *
 * @param keyId [Int]: resourceId of key to access argument
 * @param switch [SwitchCompat]: switch to check
 */
fun setSwitchUiFromArgs(context: Context, args: Bundle?, keyId: Int, switch: SwitchCompat) {
    val key: String = context.getString(keyId)
    val value: Boolean? = args?.getBoolean(key)
    if (value != null) {
        switch.isChecked = value
    }
}

fun getHistoryGroupValue(group: RadioGroup, buttons: List<RadioButton>): Int {
    val index = buttons.indexOfFirst { it.id == group.checkedRadioButtonId }
    if (index >= 0) {
        return index
    }

    return 0
}

fun setHistoryRadioGroup(context: Context, args: Bundle?, group: RadioGroup, buttons: List<RadioButton>) {
    val key: String = context.getString(R.string.key_random_history)
    val value: Int = args?.getInt(key) ?: 0
    group.check(buttons[value].id)
}

/**
 * Get information from fragment args to set initial config in UI
 */
fun setUiFromArgs(binding: ViewBinding, context: Context, args: Bundle?) {
    val shuffleNumbersSwitch: SwitchCompat
    val shuffleOperatorsSwitch: SwitchCompat
    val applyParensSwitch: SwitchCompat
    val clearOnErrorSwitch: SwitchCompat
    val applyDecimalsSwitch: SwitchCompat
    val settingsButtonSwitch: SwitchCompat
    val historyRadioGroup: RadioGroup
    val historyRadioButtons: List<RadioButton>

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
    } else {
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
}

fun saveToViewModel(viewModel: SharedViewModel, binding: ViewBinding) {
    val shuffleNumbersSwitch: SwitchCompat
    val shuffleOperatorsSwitch: SwitchCompat
    val applyParensSwitch: SwitchCompat
    val clearOnErrorSwitch: SwitchCompat
    val applyDecimalsSwitch: SwitchCompat
    val settingsButtonSwitch: SwitchCompat
    val historyRadioGroup: RadioGroup
    val historyRadioButtons: List<RadioButton>

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
    } else {
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

    viewModel.setShuffleNumbers(shuffleNumbersSwitch.isChecked)
    viewModel.setShuffleOperators(shuffleOperatorsSwitch.isChecked)
    viewModel.setApplyParens(applyParensSwitch.isChecked)
    viewModel.setClearOnError(clearOnErrorSwitch.isChecked)
    viewModel.setApplyDecimals(applyDecimalsSwitch.isChecked)
    viewModel.setShowSettingsButton(settingsButtonSwitch.isChecked)
    viewModel.setHistoryRandomness(getHistoryGroupValue(historyRadioGroup, historyRadioButtons))

}
