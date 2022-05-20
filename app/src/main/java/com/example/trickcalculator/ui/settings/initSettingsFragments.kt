package com.example.trickcalculator.ui.settings

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.trickcalculator.R
import com.example.trickcalculator.ui.main.MainFragment
import com.example.trickcalculator.ui.shared.SharedViewModel

fun initSettingsFragment(
    fragment: Fragment,
    settings: Settings,
    viewToClick: View
) {
    val context = fragment.requireContext()

    val numbersKey = context.getString(R.string.key_shuffle_numbers)
    val operatorsKey = context.getString(R.string.key_shuffle_operators)
    val parensKey = context.getString(R.string.key_apply_parens)
    val clearOnErrorKey = context.getString(R.string.key_clear_on_error)
    val decimalsKey = context.getString(R.string.key_apply_decimals)
    val settingsButtonKey = context.getString(R.string.key_settings_button)
    val mainFragmentKey = context.getString(R.string.key_main_fragment)
    val historyRandomnessKey = context.getString(R.string.key_random_history)

    viewToClick.setOnClickListener {
        val newFragment = SettingsFragment.newInstance()
        newFragment.arguments = bundleOf(
            numbersKey to settings.shuffleNumbers,
            operatorsKey to settings.shuffleOperators,
            parensKey to settings.applyParens,
            clearOnErrorKey to settings.clearOnError,
            decimalsKey to settings.applyDecimals,
            settingsButtonKey to settings.showSettingsButton,
            mainFragmentKey to (fragment is MainFragment),
            historyRandomnessKey to settings.historyRandomness
        )

        fragment.requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, newFragment)
            .addToBackStack(null)
            .commit()
    }
}

/**
 * Initialize handling of settings dialog
 */
fun initSettingsDialog(
    fragment: Fragment,
    settings: Settings,
    viewToClick: View
) {
    val context = fragment.requireContext()

    val settingsDialog = SettingsDialog()
    val numbersKey = context.getString(R.string.key_shuffle_numbers)
    val operatorsKey = context.getString(R.string.key_shuffle_operators)
    val parensKey = context.getString(R.string.key_apply_parens)
    val clearOnErrorKey = context.getString(R.string.key_clear_on_error)
    val decimalsKey = context.getString(R.string.key_apply_decimals)
    val settingsButtonKey = context.getString(R.string.key_settings_button)
    val mainFragmentKey = context.getString(R.string.key_main_fragment)
    val historyRandomnessKey = context.getString(R.string.key_random_history)

    viewToClick.setOnClickListener {
        settingsDialog.arguments = bundleOf(
            numbersKey to settings.shuffleNumbers,
            operatorsKey to settings.shuffleOperators,
            parensKey to settings.applyParens,
            clearOnErrorKey to settings.clearOnError,
            decimalsKey to settings.applyDecimals,
            settingsButtonKey to settings.showSettingsButton,
            mainFragmentKey to (fragment is MainFragment),
            historyRandomnessKey to settings.historyRandomness
        )
        settingsDialog.show(fragment.childFragmentManager, SettingsDialog.TAG)
    }
}
