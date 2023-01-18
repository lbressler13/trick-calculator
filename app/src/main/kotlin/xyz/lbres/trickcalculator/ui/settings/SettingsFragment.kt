package xyz.lbres.trickcalculator.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.databinding.FragmentSettingsBinding
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel
import xyz.lbres.trickcalculator.utils.AppLogger

/**
 * Fragment to display all configuration options for calculator
 */
class SettingsFragment : BaseFragment() {
    override var titleResId: Int = R.string.title_settings

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var historyButtons: List<RadioButton>

    override val navigateToSettings: Int? = null

    /**
     * Value indicating if the settings menu was launched from the calculator fragment
     */
    private var fromCalculatorFragment = false

    /**
     * Value indicating if the settings menu was launched through the dev tools dialog, starting on any fragment
     */
    private var fromDialog = false

    /**
     * If reset button was pressed
     */
    private var resetPressed: Boolean = false

    /**
     * If randomize button was pressed
     */
    private var randomizePressed: Boolean = false

    /**
     * If standard function button was pressed
     */
    private var standardFunctionPressed: Boolean = false

    /**
     * Initialize fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        fromDialog = arguments?.getBoolean("fromDialog") ?: false
        val backStackSize = requireParentFragment().childFragmentManager.backStackEntryCount
        fromCalculatorFragment = backStackSize == 1

        initUi()

        return binding.root
    }

    /**
     * Set values in UI based on ViewModel and add necessary onClick actions
     */
    private fun initUi() {
        historyButtons = listOf(binding.historyButton0, binding.historyButton1, binding.historyButton2, binding.historyButton3)

        binding.applyDecimalsSwitch.isChecked = sharedViewModel.applyDecimals
        binding.applyParensSwitch.isChecked = sharedViewModel.applyParens
        binding.clearOnErrorSwitch.isChecked = sharedViewModel.clearOnError
        binding.settingsButtonSwitch.isChecked = sharedViewModel.showSettingsButton
        binding.shuffleComputationSwitch.isChecked = sharedViewModel.shuffleComputation
        binding.shuffleNumbersSwitch.isChecked = sharedViewModel.shuffleNumbers
        binding.shuffleOperatorsSwitch.isChecked = sharedViewModel.shuffleOperators

        val checkedIndex = sharedViewModel.historyRandomness
        val checkedButton = if (checkedIndex in historyButtons.indices) {
            historyButtons[checkedIndex]
        } else {
            historyButtons[0]
        }
        binding.historyRandomnessGroup.check(checkedButton.id)

        binding.randomizeSettingsButton.setOnClickListener {
            randomizePressed = true
            closeFragment()
        }

        binding.resetSettingsButton.setOnClickListener {
            resetPressed = true
            closeFragment()
        }

        binding.standardFunctionButton.setOnClickListener {
            standardFunctionPressed = true
            closeFragment()
        }

        binding.settingsButtonSwitch.isVisible = fromDialog || !fromCalculatorFragment

        // close button
        binding.closeButton.root.setOnClickListener { closeFragment() }
    }

    /**
     * Save settings to ViewModel before fragment is closed
     */
    private fun saveSettingsToViewModel() {
        val randomizePressed = randomizePressed
        val resetPressed = resetPressed
        val standardFunctionPressed = standardFunctionPressed

        when {
            randomizePressed -> sharedViewModel.randomizeSettings()
            resetPressed -> {
                // persist show settings value
                sharedViewModel.showSettingsButton = binding.settingsButtonSwitch.isChecked
                sharedViewModel.resetSettings()
            }
            standardFunctionPressed -> {
                // persist show settings value
                sharedViewModel.showSettingsButton = binding.settingsButtonSwitch.isChecked
                sharedViewModel.setStandardSettings()
            }
            else -> {
                // update ViewModel based on settings selected in UI
                sharedViewModel.applyDecimals = binding.applyDecimalsSwitch.isChecked
                sharedViewModel.applyParens = binding.applyParensSwitch.isChecked
                sharedViewModel.clearOnError = binding.clearOnErrorSwitch.isChecked
                sharedViewModel.showSettingsButton = binding.settingsButtonSwitch.isChecked
                sharedViewModel.shuffleComputation = binding.shuffleComputationSwitch.isChecked
                sharedViewModel.shuffleNumbers = binding.shuffleNumbersSwitch.isChecked
                sharedViewModel.shuffleOperators = binding.shuffleOperatorsSwitch.isChecked

                val checkedId = binding.historyRandomnessGroup.checkedRadioButtonId
                val newHistoryRandomness = historyButtons.indexOfFirst { it.id == checkedId }
                sharedViewModel.setHistoryRandomness(newHistoryRandomness)
            }
        }
    }

    /**
     * Close previous fragment
     */
    private fun closePreviousFragment() {
        try {
            if (!fromDialog && !fromCalculatorFragment) {
                requireBaseActivity().popBackStack()
            }
        } catch (e: Exception) {
            // expected to fail when UI is recreating due to configuration changes or via dev tools
            AppLogger.e("Failed to close parent fragment:", e.message.toString())
        }
    }

    /**
     * Save settings to ViewModel and return to calculator screen, if not coming through dev tools
     */
    override fun onDestroy() {
        super.onDestroy()
        saveSettingsToViewModel()
        closePreviousFragment()
    }
}
