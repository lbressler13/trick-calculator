package com.example.trickcalculator.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.DialogMainSettingsBinding

class MainSettingsDialogFragment : DialogFragment() {
    private lateinit var binding: DialogMainSettingsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogMainSettingsBinding.inflate(layoutInflater)
        val root = binding.root

        setUiFromArgs()

        val doneText = requireContext().getString(R.string.done)
        val title = requireContext().getString(R.string.title_settings)

        return AlertDialog.Builder(requireContext())
            .setView(root)
            .setMessage(title)
            .setPositiveButton(doneText) { _, _ -> }
            .create()
    }

    private fun setUiFromArgs() {
        setSwitchUiFromArgs(R.string.key_shuffle_numbers, binding.shuffleNumbersSwitch)
        setSwitchUiFromArgs(R.string.key_shuffle_operators, binding.shuffleOperatorsSwitch)
        setSwitchUiFromArgs(R.string.key_apply_parens, binding.applyParensSwitch)
        setSwitchUiFromArgs(R.string.key_clear_on_error, binding.clearOnErrorSwitch)
    }

    private fun setSwitchUiFromArgs(keyId: Int, switch: SwitchCompat) {
        val key: String = requireContext().getString(keyId)
        val value: Boolean? = this.arguments?.getBoolean(key)
        if (value != null) {
            switch.isChecked = value
        }
    }

    private fun bundleSettings(): Bundle {
        val numbersKey = requireContext().getString(R.string.key_shuffle_numbers)
        val operatorsKey = requireContext().getString(R.string.key_shuffle_operators)
        val parensKey = requireContext().getString(R.string.key_apply_parens)
        val clearOnErrorKey = requireContext().getString(R.string.key_clear_on_error)
        return bundleOf(
            numbersKey to binding.shuffleNumbersSwitch.isChecked,
            operatorsKey to binding.shuffleOperatorsSwitch.isChecked,
            parensKey to binding.applyParensSwitch.isChecked,
            clearOnErrorKey to binding.clearOnErrorSwitch.isChecked
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        val requestKey = requireContext().getString(R.string.key_settings_request)

        parentFragmentManager.setFragmentResult(
            requestKey,
            bundleSettings()
        )

        super.onDismiss(dialog)
    }

    companion object {
        const val TAG = "MainSettingsDialog"
    }
}