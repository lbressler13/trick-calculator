package com.example.trickcalculator.ui.settings

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.DialogSharedSettingsBinding
import com.example.trickcalculator.ui.shared.SharedViewModel

/**
 * DialogFragment to display all configuration options for calculator
 */
class SettingsDialog : DialogFragment() {
    private lateinit var binding: DialogSharedSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel

    /**
     * Initialize dialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSharedSettingsBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setUiFromArgs()

        val doneText = requireContext().getString(R.string.done)
        val title = requireContext().getString(R.string.title_settings)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setMessage(title)
            .setPositiveButton(doneText) { _, _ -> }
            .create()
    }

    /**
     * Get information from fragment args to set initial config in UI
     */
    private fun setUiFromArgs() {
        setSwitchUiFromArgs(R.string.key_shuffle_numbers, binding.shuffleNumbersSwitch)
        setSwitchUiFromArgs(R.string.key_shuffle_operators, binding.shuffleOperatorsSwitch)
        setSwitchUiFromArgs(R.string.key_apply_parens, binding.applyParensSwitch)
        setSwitchUiFromArgs(R.string.key_clear_on_error, binding.clearOnErrorSwitch)
        setSwitchUiFromArgs(R.string.key_apply_decimals, binding.applyDecimalsSwitch)
        setSwitchUiFromArgs(R.string.key_settings_button, binding.settingsButtonSwitch)

        val mainFragmentKey = requireContext().getString(R.string.key_main_fragment)
        val isMainFragment = this.arguments?.getBoolean(mainFragmentKey)
        binding.settingsButtonSwitch.isVisible = isMainFragment != true

        setHistoryRadioGroup()
    }

    /**
     * Get information from fragment args to set if a switch should be checked
     *
     * @param keyId [Int]: resourceId of key to access argument
     * @param switch [SwitchCompat]: switch to check
     */
    private fun setSwitchUiFromArgs(keyId: Int, switch: SwitchCompat) {
        val key: String = requireContext().getString(keyId)
        val value: Boolean? = this.arguments?.getBoolean(key)
        if (value != null) {
            switch.isChecked = value
        }
    }

    private fun setHistoryRadioGroup() {
        val group = binding.historyRandomnessGroup
        val key: String = requireContext().getString(R.string.key_random_history)
        val value: Int = this.arguments?.getInt(key) ?: 0
        when (value) {
            0 -> group.check(binding.historyButton0.id)
            1 -> group.check(binding.historyButton1.id)
            2 -> group.check(binding.historyButton2.id)
            3 -> group.check(binding.historyButton3.id)
        }
    }

    private fun getHistoryGroupValue(): Int {
        return when (binding.historyRandomnessGroup.checkedRadioButtonId) {
            binding.historyButton0.id -> 0
            binding.historyButton1.id -> 1
            binding.historyButton2.id -> 2
            binding.historyButton3.id -> 3
            else -> 0
        }
    }

    /**
     * Create bundle of settings to pass in fragment result
     *
     * @return bundle of current setting config in UI
     */
    private fun bundleSettings(): Bundle {
        val numbersKey = requireContext().getString(R.string.key_shuffle_numbers)
        val operatorsKey = requireContext().getString(R.string.key_shuffle_operators)
        val parensKey = requireContext().getString(R.string.key_apply_parens)
        val clearOnErrorKey = requireContext().getString(R.string.key_clear_on_error)
        val decimalsKey = requireContext().getString(R.string.key_apply_decimals)
        val settingsButtonKey = requireContext().getString(R.string.key_settings_button)
        val historyRandomnessKey = requireContext().getString(R.string.key_random_history)
        return bundleOf(
            numbersKey to binding.shuffleNumbersSwitch.isChecked,
            operatorsKey to binding.shuffleOperatorsSwitch.isChecked,
            parensKey to binding.applyParensSwitch.isChecked,
            clearOnErrorKey to binding.clearOnErrorSwitch.isChecked,
            decimalsKey to binding.applyDecimalsSwitch.isChecked,
            settingsButtonKey to binding.settingsButtonSwitch.isChecked,
            historyRandomnessKey to getHistoryGroupValue()
        )
    }

    /**
     * Set fragment result to be seen by parent
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        sharedViewModel.setShuffleNumbers(binding.shuffleNumbersSwitch.isChecked)
        sharedViewModel.setShuffleOperators(binding.shuffleOperatorsSwitch.isChecked)
        sharedViewModel.setApplyParens(binding.applyParensSwitch.isChecked)
        sharedViewModel.setClearOnError(binding.clearOnErrorSwitch.isChecked)
        sharedViewModel.setApplyDecimals(binding.applyDecimalsSwitch.isChecked)
        sharedViewModel.setShowSettingsButton(binding.settingsButtonSwitch.isChecked)
        sharedViewModel.setHistoryRandomness(getHistoryGroupValue())
    }

    companion object {
        // tag is required when showing fragment
        const val TAG = "SettingsDialog"
    }
}