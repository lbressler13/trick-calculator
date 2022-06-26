package com.example.trickcalculator.ui.settings.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentSettingsBinding
import com.example.trickcalculator.ui.ActivityFragment
import com.example.trickcalculator.ui.shared.SharedViewModel

class SettingsFragment : ActivityFragment(), SettingsUI {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel
    override var titleResId: Int = R.string.title_settings

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

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        initUiElements()

        specializedFragmentCode()
        setUiFromArgs(this, sharedViewModel)

        return binding.root
    }

    private fun initUiElements() {
        shuffleNumbersSwitch = binding.shuffleNumbersSwitch
        shuffleOperatorsSwitch = binding.shuffleOperatorsSwitch
        applyParensSwitch = binding.applyParensSwitch
        clearOnErrorSwitch = binding.clearOnErrorSwitch
        applyDecimalsSwitch = binding.applyDecimalsSwitch
        shuffleComputationSwitch = binding.shuffleComputationSwitch
        settingsButtonSwitch = binding.settingsButtonSwitch
        resetSettingsButton = binding.resetSettingsButton
        randomizeSettingsButton = binding.randomizeSettingsButton

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
        binding.closeButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveToViewModel(this, sharedViewModel)
        closePreviousFragment(this)
    }
}
