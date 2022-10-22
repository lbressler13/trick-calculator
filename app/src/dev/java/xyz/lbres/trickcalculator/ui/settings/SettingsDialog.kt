package xyz.lbres.trickcalculator.ui.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.databinding.DialogSettingsBinding
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel

/**
 * DialogFragment to display all configuration options for calculator
 */
class SettingsDialog : DialogFragment() {
    private lateinit var binding: DialogSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var settingsUI: SettingsUI

    /**
     * Build dialog, comes before onCreateView and dialog is not connected to context
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSettingsBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val doneText = requireContext().getString(R.string.done)
        val title = requireContext().getString(R.string.title_settings)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setMessage(title)
            .setPositiveButton(doneText) { _, _ -> }
            .create()
    }

    /**
     * Continue initialization after view is connected to context
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        settingsUI = SettingsUI(this, binding.root, sharedViewModel, viewLifecycleOwner)
        specializedFragmentCode()

        return binding.root
    }

    /**
     * Code that is run in dialog but not fragment
     */
    private fun specializedFragmentCode() {
        settingsUI.showSettingsButtonSwitch()
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsUI.saveSettingsToViewModel()
        settingsUI.closePreviousFragment()
    }

    companion object {
        // tag is required when showing fragment
        const val TAG = "SettingsDialog"
    }
}
