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
import com.example.trickcalculator.databinding.DialogSettingsBinding
import com.example.trickcalculator.ui.shared.SharedViewModel

/**
 * DialogFragment to display all configuration options for calculator
 */
class SettingsDialog : DialogFragment() {
    private lateinit var binding: DialogSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel
    var resetPressed = false
    var randomizePressed = false

    /**
     * Initialize dialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSettingsBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setUiFromArgs(this, sharedViewModel, binding)

        val doneText = requireContext().getString(R.string.done)
        val title = requireContext().getString(R.string.title_settings)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setMessage(title)
            .setPositiveButton(doneText) { _, _ -> }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        saveToViewModel(this, sharedViewModel, binding)
        closeParentDialog()
    }

    private fun closeParentDialog() {
        if (parentFragment != null && parentFragment is DialogFragment) {
            (parentFragment as DialogFragment).dismiss()
        }
    }

    companion object {
        // tag is required when showing fragment
        const val TAG = "SettingsDialog"
    }
}
