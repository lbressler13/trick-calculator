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

// TODO don't pass root view explicitly
class SettingsUI(private val fragment: Fragment, private val rootView: View, private val viewModel: SharedViewModel, private val lifecycleOwner: LifecycleOwner) {
    /**
     * Flags for events in fragment
     */
    private var resetPressed: Boolean = false
    private var randomizePressed: Boolean = false

    /**
     * UI elements to represent settings
     */
    // switches
    private lateinit var applyDecimalsSwitch: SwitchCompat
    private lateinit var applyParensSwitch: SwitchCompat
    private lateinit var clearOnErrorSwitch: SwitchCompat
    private lateinit var settingsButtonSwitch: SwitchCompat
    private lateinit var shuffleComputationSwitch: SwitchCompat
    private lateinit var shuffleNumbersSwitch: SwitchCompat
    private lateinit var shuffleOperatorsSwitch: SwitchCompat

    // radio group
    private lateinit var historyRadioGroup: RadioGroup
    private lateinit var historyRadioButtons: List<RadioButton>

    // all settings buttons
    private lateinit var randomizeSettingsButton: View
    private lateinit var resetSettingsButton: View

    init {
        collectUI()
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
    }

    private fun collectUI() {
        applyDecimalsSwitch = rootView.findViewById(R.id.applyDecimalsSwitch)
        applyParensSwitch = rootView.findViewById(R.id.applyParensSwitch)
        clearOnErrorSwitch = rootView.findViewById(R.id.clearOnErrorSwitch)
        settingsButtonSwitch = rootView.findViewById(R.id.settingsButtonSwitch)
        shuffleComputationSwitch = rootView.findViewById(R.id.shuffleComputationSwitch)
        shuffleNumbersSwitch = rootView.findViewById(R.id.shuffleNumbersSwitch)
        shuffleOperatorsSwitch = rootView.findViewById(R.id.shuffleOperatorsSwitch)

        historyRadioGroup = rootView.findViewById(R.id.historyRandomnessGroup)
        historyRadioButtons = listOf(
            rootView.findViewById(R.id.historyButton0),
            rootView.findViewById(R.id.historyButton1),
            rootView.findViewById(R.id.historyButton2),
            rootView.findViewById(R.id.historyButton3),
        )

        randomizeSettingsButton = rootView.findViewById(R.id.randomizeSettingsButton)
        resetSettingsButton = rootView.findViewById(R.id.resetSettingsButton)
    }

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

    private fun closeCurrentFragment() {
        when (fragment) {
            is DialogFragment -> fragment.dismiss()
            is BaseFragment -> fragment.requireMainActivity().popBackStack()
        }
    }

    fun saveSettingsToViewModel() {
        val randomizedPressed = randomizePressed
        val resetPressed = resetPressed

        when {
            randomizedPressed -> viewModel.randomizeSettings()
            resetPressed -> {
                // save changes to visibility of show settings
                viewModel.setShowSettingsButton(settingsButtonSwitch.isChecked)
                viewModel.resetSettings()
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

    fun closePreviousFragment() {
        try {
            if (fragment is DialogFragment && fragment.requireParentFragment() is DialogFragment) {
                (fragment.requireParentFragment() as DialogFragment).dismiss()
            } else if (fragment !is DialogFragment && fragment.parentFragmentManager.backStackEntryCount > 0) {
                (fragment as BaseFragment).requireMainActivity().popBackStack()
            }
        } catch (e: Exception) {
            // expected to fail when ui is recreating due to configuration changes or via dev tools
            AppLogger.e("Failed to close parent fragment:", e.message.toString())
        }
    }

    fun showSettingsButtonSwitch() {
        settingsButtonSwitch.visible()
    }
}
