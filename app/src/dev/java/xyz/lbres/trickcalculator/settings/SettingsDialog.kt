package xyz.lbres.trickcalculator.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.databinding.DialogSettingsBinding
import xyz.lbres.trickcalculator.ui.settings.components.*
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel

/**
 * DialogFragment to display all configuration options for calculator
 */
class SettingsDialog : DialogFragment(), SettingsUI {
    private lateinit var binding: DialogSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel

    override var showSettingsButtonSwitch: Boolean = true
    override var randomizePressed = false
    override var resetPressed = false

    override lateinit var applyDecimalsSwitch: SwitchCompat
    override lateinit var applyParensSwitch: SwitchCompat
    override lateinit var clearOnErrorSwitch: SwitchCompat
    override lateinit var historyRadioGroup: RadioGroup
    override lateinit var historyRadioButtons: List<RadioButton>
    override lateinit var randomizeSettingsButton: View
    override lateinit var resetSettingsButton: View
    override lateinit var settingsButtonSwitch: SwitchCompat
    override lateinit var shuffleComputationSwitch: SwitchCompat
    override lateinit var shuffleNumbersSwitch: SwitchCompat
    override lateinit var shuffleOperatorsSwitch: SwitchCompat

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
        collectUiElements()
        initSettingsUi(sharedViewModel, viewLifecycleOwner)

        return binding.root
    }

    // assign UI elements for SettingsUI, after binding has been initialized
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
        saveToViewModel(sharedViewModel)
        closePreviousFragment()
    }

    companion object {
        // tag is required when showing fragment
        const val TAG = "SettingsDialog"
    }
}
