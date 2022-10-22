package xyz.lbres.trickcalculator.ui.settings.components

import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel
import xyz.lbres.trickcalculator.utils.AppLogger

fun SettingsUI.initSettingsUi(viewModel: SharedViewModel, lifecycleOwner: LifecycleOwner) {
    initObservers(viewModel, lifecycleOwner)

    settingsButtonSwitch.isVisible = showSettingsButtonSwitch

    randomizeSettingsButton.setOnClickListener {
        randomizePressed = true
        closeCurrentFragment()

    }
    resetSettingsButton.setOnClickListener {
        resetPressed = true
        closeCurrentFragment()
    }
}

private fun SettingsUI.initObservers(viewModel: SharedViewModel, lifecycleOwner: LifecycleOwner) {
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

// TODO this may cause a crash
private fun SettingsUI.closeCurrentFragment() {
    when (this as Fragment) {
        is DialogFragment -> (this as DialogFragment).dismiss()
        is BaseFragment -> (this as BaseFragment).requireMainActivity().popBackStack()
    }
}

fun SettingsUI.saveToViewModel(viewModel: SharedViewModel) {
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

fun SettingsUI.closePreviousFragment() {
    try {
        if (this is DialogFragment && requireParentFragment() is DialogFragment) {
            (requireParentFragment() as DialogFragment).dismiss()
        } else if (this !is DialogFragment && this is Fragment && parentFragmentManager.backStackEntryCount > 0) {
            (this as BaseFragment).requireMainActivity().popBackStack()
        }
    } catch (e: Exception) {
        // expected to fail when ui is recreating due to configuration changes or via dev tools
        AppLogger.e("Failed to close parent fragment:", e.message.toString())
    }
}
