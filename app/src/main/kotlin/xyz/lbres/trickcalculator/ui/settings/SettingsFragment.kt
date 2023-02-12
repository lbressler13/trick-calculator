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
import xyz.lbres.trickcalculator.utils.AppLogger

/**
 * Fragment to display all configuration options for calculator
 */
class SettingsFragment : BaseFragment() {
    override var titleResId: Int = R.string.title_settings

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel
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
     * If settings were saved before [saveSettingsToViewModel] was called, such as by using reset button
     */
    private var settingsPreSaved: Boolean = false

    /**
     * Initialize fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]

        val fromDialogKey = getString(R.string.from_dialog_key)
        fromDialog = arguments?.getBoolean(fromDialogKey) ?: false
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

        binding.applyDecimalsSwitch.isChecked = viewModel.applyDecimals
        binding.applyParensSwitch.isChecked = viewModel.applyParens
        binding.clearOnErrorSwitch.isChecked = viewModel.clearOnError
        binding.settingsButtonSwitch.isChecked = viewModel.showSettingsButton
        binding.shuffleComputationSwitch.isChecked = viewModel.shuffleComputation
        binding.shuffleNumbersSwitch.isChecked = viewModel.shuffleNumbers
        binding.shuffleOperatorsSwitch.isChecked = viewModel.shuffleOperators

        val checkedIndex = viewModel.historyRandomness
        val checkedButton = if (checkedIndex in historyButtons.indices) {
            historyButtons[checkedIndex]
        } else {
            historyButtons[0]
        }
        binding.historyRandomnessGroup.check(checkedButton.id)

        binding.settingsButtonSwitch.isVisible = fromDialog || !fromCalculatorFragment

        // close button
        binding.closeButton.root.setOnClickListener { closeFragment() }

        setButtonActions()
    }

    /**
     * Assign onClick actions to buttons that affect all settings
     */
    private fun setButtonActions() {
        binding.randomizeSettingsButton.setOnClickListener {
            viewModel.randomizeSettings()
            settingsPreSaved = true
            closeFragment()
        }

        binding.resetSettingsButton.setOnClickListener {
            // persist show settings value
            viewModel.showSettingsButton = binding.settingsButtonSwitch.isChecked
            viewModel.resetSettings()

            settingsPreSaved = true
            closeFragment()
        }

        binding.standardFunctionButton.setOnClickListener {
            viewModel.showSettingsButton = binding.settingsButtonSwitch.isChecked
            viewModel.setStandardSettings()

            settingsPreSaved = true
            closeFragment()
        }
    }

    /**
     * Save settings to ViewModel based on selections in UI
     */
    private fun saveSettingsToViewModel() {
        viewModel.applyDecimals = binding.applyDecimalsSwitch.isChecked
        viewModel.applyParens = binding.applyParensSwitch.isChecked
        viewModel.clearOnError = binding.clearOnErrorSwitch.isChecked
        viewModel.showSettingsButton = binding.settingsButtonSwitch.isChecked
        viewModel.shuffleComputation = binding.shuffleComputationSwitch.isChecked
        viewModel.shuffleNumbers = binding.shuffleNumbersSwitch.isChecked
        viewModel.shuffleOperators = binding.shuffleOperatorsSwitch.isChecked

        val checkedId = binding.historyRandomnessGroup.checkedRadioButtonId
        val newHistoryRandomness = historyButtons.indexOfFirst { it.id == checkedId }
        viewModel.setHistoryRandomness(newHistoryRandomness)
    }

    /**
     * Close previous fragment
     */
    private fun closePreviousFragment() {
        try {
            requireBaseActivity().popBackStack()
        } catch (e: Exception) {
            // expected to fail when UI is recreating due to configuration changes or via dev tools
            val appName = getString(R.string.app_name)
            AppLogger.e(appName, "Failed to close parent fragment: ${e.message}")
        }
    }

    /**
     * Save settings to ViewModel and return to calculator screen, if not coming through dev tools
     */
    override fun onDestroy() {
        super.onDestroy()

        if (!settingsPreSaved) {
            saveSettingsToViewModel()
        }

        if (!fromDialog && !fromCalculatorFragment) {
            closePreviousFragment()
        }
    }
}
