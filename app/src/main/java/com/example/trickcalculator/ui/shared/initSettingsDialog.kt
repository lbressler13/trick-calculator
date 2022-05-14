package com.example.trickcalculator.ui.shared

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.trickcalculator.R
import com.example.trickcalculator.ui.main.MainFragment

/**
 * Initialize handling of settings dialog
 */
fun initSettingsDialog(
    fragment: Fragment,
    viewModel: SharedViewModel,
    settings: Settings,
    viewToClick: View
) {
    val context = fragment.requireContext()

    val settingsDialog = SharedSettingsDialog()
    val numbersKey = context.getString(R.string.key_shuffle_numbers)
    val operatorsKey = context.getString(R.string.key_shuffle_operators)
    val parensKey = context.getString(R.string.key_apply_parens)
    val clearOnErrorKey = context.getString(R.string.key_clear_on_error)
    val decimalsKey = context.getString(R.string.key_apply_decimals)
    val settingsButtonKey = context.getString(R.string.key_settings_button)
    val mainFragmentKey = context.getString(R.string.key_main_fragment)
    val historyRandomnessKey = context.getString(R.string.key_history)
    val requestKey = context.getString(R.string.key_settings_request)

    // update viewmodel with response from dialog
    fragment.childFragmentManager.setFragmentResultListener(
        requestKey,
        fragment.viewLifecycleOwner,
        { _: String, result: Bundle ->
            val returnedShuffleNumbers: Boolean = result.getBoolean(numbersKey, settings.shuffleNumbers)
            viewModel.setShuffleNumbers(returnedShuffleNumbers)

            val returnedShuffleOperators: Boolean =
                result.getBoolean(operatorsKey, settings.shuffleOperators)
            viewModel.setShuffleOperators(returnedShuffleOperators)

            val returnedApplyParens: Boolean = result.getBoolean(parensKey, settings.applyParens)
            viewModel.setApplyParens(returnedApplyParens)

            val returnedClearOnError: Boolean = result.getBoolean(clearOnErrorKey, settings.clearOnError)
            viewModel.setClearOnError(returnedClearOnError)

            val returnedApplyDecimals: Boolean = result.getBoolean(decimalsKey, settings.applyDecimals)
            viewModel.setApplyDecimals(returnedApplyDecimals)

            val returnedShowSettingsButton: Boolean = result.getBoolean(settingsButtonKey, settings.showSettingsButton)
            viewModel.setShowSettingsButton(returnedShowSettingsButton)

            val returnedHistoryRandomness: Int = result.getInt(historyRandomnessKey, settings.historyRandomness)
            viewModel.setHistoryRandomness(returnedHistoryRandomness)
        }
    )

    viewToClick.setOnClickListener {
        settingsDialog.arguments = bundleOf(
            numbersKey to settings.shuffleNumbers,
            operatorsKey to settings.shuffleOperators,
            parensKey to settings.applyParens,
            clearOnErrorKey to settings.clearOnError,
            decimalsKey to settings.applyDecimals,
            mainFragmentKey to (fragment is MainFragment),
            historyRandomnessKey to settings.historyRandomness
        )
        settingsDialog.show(fragment.childFragmentManager, SharedSettingsDialog.TAG)
    }
}
