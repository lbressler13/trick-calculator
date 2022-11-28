package xyz.lbres.trickcalculator.ui.settings

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel
import xyz.lbres.trickcalculator.utils.AppLogger
import xyz.lbres.trickcalculator.utils.gone
import xyz.lbres.trickcalculator.utils.visible

/**
 * Class to handle UI interactions in a settings fragment.
 * Reduces specialized code in fragments.
 *
 * @param fragment [Fragment]: the fragment creating the UI
 * @param rootView [View]: root view of the fragment, expected to contain Views for updating settings.
 * Must be passed explicitly, because fragment view is not available until after onCreateView returns.
 * @param viewModel [SharedViewModel]: ViewModel that contains info about settings and can be used to modify them
 * @param lifecycleOwner [LifecycleOwner]: lifecycle owner to use when observing changes in ViewModel
 */
class SettingsUI(private val fragment: Fragment, rootView: View, private val viewModel: SharedViewModel, private val lifecycleOwner: LifecycleOwner) {
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
     * Switches to control settings
     */
    private val applyDecimalsSwitch: SwitchCompat = rootView.findViewById(R.id.applyDecimalsSwitch)
    private val applyParensSwitch: SwitchCompat = rootView.findViewById(R.id.applyParensSwitch)
    private val clearOnErrorSwitch: SwitchCompat = rootView.findViewById(R.id.clearOnErrorSwitch)
    private val settingsButtonSwitch: SwitchCompat = rootView.findViewById(R.id.settingsButtonSwitch)
    private val shuffleComputationSwitch: SwitchCompat = rootView.findViewById(R.id.shuffleComputationSwitch)
    private val shuffleNumbersSwitch: SwitchCompat = rootView.findViewById(R.id.shuffleNumbersSwitch)
    private val shuffleOperatorsSwitch: SwitchCompat = rootView.findViewById(R.id.shuffleOperatorsSwitch)

    /**
     * Radio group for history randomness settings
     */
    private val historyRadioGroup: RadioGroup = rootView.findViewById(R.id.historyRandomnessGroup)
    private val historyRadioButtons: List<RadioButton> = listOf(
        rootView.findViewById(R.id.historyButton0),
        rootView.findViewById(R.id.historyButton1),
        rootView.findViewById(R.id.historyButton2),
        rootView.findViewById(R.id.historyButton3),
    )

    /**
     * Button to modify all settings
     */
    private val randomizeSettingsButton: View = rootView.findViewById(R.id.randomizeSettingsButton)
    private val resetSettingsButton: View = rootView.findViewById(R.id.resetSettingsButton)
    private val standardFunctionButton: View = rootView.findViewById(R.id.standardFunctionButton)

    /**
     * Initialize UI elements, observers, and on click functions for buttons
     */
    init {
        settingsButtonSwitch.gone()

        initObservers()

        randomizeSettingsButton.setOnClickListener {
            randomizePressed = true
            closeCurrentFragment()
        }

        resetSettingsButton.setOnClickListener {
            resetPressed = true
            closeCurrentFragment()
        }

        standardFunctionButton.setOnClickListener {
            standardFunctionPressed = true
            closeCurrentFragment()
        }
    }

    /**
     * Initialize observers for updating settings in UI.
     * Changes must be observed because dialog may update settings while fragment is open.
     */
    private fun initObservers() {
        // update switches
        viewModel.applyDecimals.observe(lifecycleOwner) { applyDecimalsSwitch.isChecked = it }
        viewModel.applyParens.observe(lifecycleOwner) { applyParensSwitch.isChecked = it }
        viewModel.clearOnError.observe(lifecycleOwner) { clearOnErrorSwitch.isChecked = it }
        viewModel.showSettingsButton.observe(lifecycleOwner) { settingsButtonSwitch.isChecked = it }
        viewModel.shuffleComputation.observe(lifecycleOwner) { shuffleComputationSwitch.isChecked = it }
        viewModel.shuffleNumbers.observe(lifecycleOwner) { shuffleNumbersSwitch.isChecked = it }
        viewModel.shuffleOperators.observe(lifecycleOwner) { shuffleOperatorsSwitch.isChecked = it }

        // update radio group
        viewModel.historyRandomness.observe(lifecycleOwner) { historyRadioButtons[it].isChecked = true }
    }

    /**
     * Close the current fragment
     */
    private fun closeCurrentFragment() {
        when (fragment) {
            is DialogFragment -> fragment.dismiss()
            is BaseFragment -> fragment.requireBaseActivity().popBackStack()
        }
    }

    /**
     * Use settings in UI to update ViewModel
     */
    fun saveSettingsToViewModel() {
        val randomizedPressed = randomizePressed
        val resetPressed = resetPressed
        val standardFunctionPressed = standardFunctionPressed

        when {
            randomizedPressed -> viewModel.randomizeSettings()
            resetPressed -> {
                // persist show settings value
                viewModel.setShowSettingsButton(settingsButtonSwitch.isChecked)
                viewModel.resetSettings()
            }
            standardFunctionPressed -> {
                // persist show settings value
                viewModel.setShowSettingsButton(settingsButtonSwitch.isChecked)
                viewModel.setStandardSettings()
            }
            else -> {
                // update ViewModel based on settings selected in UI
                viewModel.setApplyDecimals(applyDecimalsSwitch.isChecked)
                viewModel.setApplyParens(applyParensSwitch.isChecked)
                viewModel.setClearOnError(clearOnErrorSwitch.isChecked)
                viewModel.setShowSettingsButton(settingsButtonSwitch.isChecked)
                viewModel.setShuffleComputation(shuffleComputationSwitch.isChecked)
                viewModel.setShuffleNumbers(shuffleNumbersSwitch.isChecked)
                viewModel.setShuffleOperators(shuffleOperatorsSwitch.isChecked)

                val checkedId = historyRadioGroup.checkedRadioButtonId
                viewModel.setHistoryRandomness(historyRadioButtons.indexOfFirst { it.id == checkedId })
            }
        }
    }

    /**
     * Close the previous fragment if applicable
     */
    fun closePreviousFragment() {
        try {
            if (fragment is DialogFragment && fragment.requireParentFragment() is DialogFragment) {
                (fragment.requireParentFragment() as DialogFragment).dismiss()
            } else if (fragment is BaseFragment && fragment.parentFragmentManager.backStackEntryCount > 0) {
                fragment.requireBaseActivity().popBackStack()
            }
        } catch (e: Exception) {
            // expected to fail when UI is recreating due to configuration changes or via dev tools
            AppLogger.e("Failed to close parent fragment:", e.message.toString())
        }
    }

    /**
     * Make settings button switch visible
     */
    fun showSettingsButtonSwitch() {
        settingsButtonSwitch.visible()
    }
}
