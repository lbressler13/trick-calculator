package com.example.trickcalculator.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.DialogDeveloperToolsBinding
import com.example.trickcalculator.ui.settings.Settings
import com.example.trickcalculator.ui.shared.SharedViewModel
import com.example.trickcalculator.ui.settings.initSettingsDialog
import com.example.trickcalculator.ui.settings.initSettingsObservers

class DeveloperToolsDialog : DialogFragment() {
    private lateinit var binding: DialogDeveloperToolsBinding
    private lateinit var viewModel: SharedViewModel
    private val settings = Settings()

    /**
     * Initialize dialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeveloperToolsBinding.inflate(layoutInflater)
        val root = binding.root

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val doneText = requireContext().getString(R.string.done)
        val title = requireContext().getString(R.string.title_dev_tools)

        return AlertDialog.Builder(requireContext())
            .setView(root)
            .setMessage(title)
            .setPositiveButton(doneText) { _, _ -> }
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.clearHistoryButton.setOnClickListener { viewModel.clearHistory() }

        binding.refreshUIButton.setOnClickListener {
            (requireActivity() as MainActivity).recreate()
        }

        initSettingsObservers(settings, viewModel, viewLifecycleOwner)
        initSettingsDialog(this, settings, binding.settingsDialogButton)

        return binding.root
    }

    companion object {
        // tag is required when showing fragment
        const val TAG = "DeveloperToolsDialog"
    }
}