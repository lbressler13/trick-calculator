package com.example.trickcalculator.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
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
        val args = this.arguments

        val numbersKey = requireContext().getString(R.string.key_shuffle_numbers)
        val shuffleNumbers: Boolean? = args?.getBoolean(numbersKey)
        if (shuffleNumbers != null) {
            binding.shuffleNumbersSwitch.isChecked = shuffleNumbers
        }

        val operatorsKey = requireContext().getString(R.string.key_shuffle_operators)
        val shuffleOperators: Boolean? = args?.getBoolean(operatorsKey)
        if (shuffleOperators != null) {
            binding.shuffleOperatorsSwitch.isChecked = shuffleOperators
        }
    }

    private fun bundleSettings(): Bundle {
        val numbersKey = requireContext().getString(R.string.key_shuffle_numbers)
        val operatorsKey = requireContext().getString(R.string.key_shuffle_operators)
        return bundleOf(
            numbersKey to binding.shuffleNumbersSwitch.isChecked,
            operatorsKey to binding.shuffleOperatorsSwitch.isChecked
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