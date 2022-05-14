package com.example.trickcalculator.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.DialogDeveloperToolsBinding
import com.example.trickcalculator.ui.shared.SharedViewModel

class DeveloperToolsDialog : DialogFragment() {
    private lateinit var binding: DialogDeveloperToolsBinding
    private lateinit var viewModel: SharedViewModel

    /**
     * Initialize dialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeveloperToolsBinding.inflate(layoutInflater)
        val root = binding.root

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val doneText = requireContext().getString(R.string.done)
        val title = requireContext().getString(R.string.title_dev_tools)

        initUI()

        return AlertDialog.Builder(requireContext())
            .setView(root)
            .setMessage(title)
            .setPositiveButton(doneText) { _, _ -> }
            .create()
    }

    private fun initUI() {
        binding.clearHistoryButton.setOnClickListener { viewModel.clearHistory() }

        binding.refreshUIButton.setOnClickListener {
            (requireActivity() as MainActivity).recreate()
        }
    }

    companion object {
        // tag is required when showing fragment
        const val TAG = "DeveloperToolsDialog"
    }
}