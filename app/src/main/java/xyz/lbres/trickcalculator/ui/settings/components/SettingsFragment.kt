package xyz.lbres.trickcalculator.ui.settings.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.databinding.FragmentSettingsBinding
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel

/**
 * DialogFragment to display all configuration options for calculator
 */
class SettingsFragment : BaseFragment(), SettingsUI {
    override var titleResId: Int = R.string.title_settings // fragment-specific value

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel

    override var showSettingsButtonSwitch: Boolean = false
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        specializedFragmentCode()

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

    // code that is run in fragment but not dialog
    private fun specializedFragmentCode() {
        // show or hide settings button based on number of previous fragments
        val backStackSize = requireParentFragment().childFragmentManager.backStackEntryCount
        showSettingsButtonSwitch = backStackSize > 1

        // close button
        binding.closeButton.setOnClickListener { requireMainActivity().popBackStack() }

        // save settings when another fragment is opened
        // preserves current settings when dialog is opened
        childFragmentManager.addFragmentOnAttachListener { _, _ ->
            saveToViewModel(sharedViewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveToViewModel(sharedViewModel)
        closePreviousFragment()
    }
}
