package com.example.trickcalculator.ui.settings.components

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.DialogSettingsBinding
import com.example.trickcalculator.ui.shared.SharedViewModel

/**
 * DialogFragment to display all configuration options for calculator
 */
class SettingsDialog : DialogFragment(), SettingsUI {
    private lateinit var binding: DialogSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel

    override val fragment = this
    override var resetPressed = false
    override var randomizePressed = false

    override lateinit var shuffleNumbersSwitch: SwitchCompat
    override lateinit var shuffleOperatorsSwitch: SwitchCompat
    override lateinit var applyParensSwitch: SwitchCompat
    override lateinit var clearOnErrorSwitch: SwitchCompat
    override lateinit var applyDecimalsSwitch: SwitchCompat
    override lateinit var shuffleComputationSwitch: SwitchCompat
    override lateinit var settingsButtonSwitch: SwitchCompat
    override lateinit var historyRadioGroup: RadioGroup
    override lateinit var historyRadioButtons: List<RadioButton>
    override lateinit var resetSettingsButton: View
    override lateinit var randomizeSettingsButton: View

    /**
     * Initialize dialog
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        collectUiElements()
        initObservers(this, sharedViewModel, viewLifecycleOwner)
        initUi(this)

        return binding.root
    }

    private fun collectUiElements() {
        applyDecimalsSwitch = binding.applyDecimalsSwitch
        applyParensSwitch = binding.applyParensSwitch
        clearOnErrorSwitch = binding.clearOnErrorSwitch
        randomizeSettingsButton = binding.randomizeSettingsButton
        resetSettingsButton = binding.resetSettingsButton
        settingsButtonSwitch = binding.settingsButtonSwitch
        shuffleComputationSwitch = binding.shuffleComputationSwitch
        shuffleNumbersSwitch = binding.shuffleNumbersSwitch
        shuffleOperatorsSwitch = binding.shuffleOperatorsSwitch

        historyRadioGroup = binding.historyRandomnessGroup
        historyRadioButtons = listOf(
            binding.historyButton0,
            binding.historyButton1,
            binding.historyButton2,
            binding.historyButton3
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        saveToViewModel(this, sharedViewModel)
        closePreviousFragment(this)
    }

    companion object {
        // tag is required when showing fragment
        const val TAG = "SettingsDialog"
    }
}
