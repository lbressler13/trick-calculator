package com.example.trickcalculator.ui.settings

import android.content.Context
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.trickcalculator.R
import com.example.trickcalculator.ui.main.MainFragment

/**
 * Initialize listener to launch settings fragment
 *
 * @param fragment [Fragment]: calling fragment
 * @param settings [Settings]: object containing current settings
 * @param viewToClick [View]: view which should launch settings fragment when clicked
 */
fun initSettingsFragment(
    fragment: Fragment,
    settings: Settings,
    viewToClick: View
) {
    val context = fragment.requireContext()

    viewToClick.setOnClickListener {
        val newFragment = SettingsFragment.newInstance()
        addArgsToFragment(context, settings, newFragment)

        fragment.requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, newFragment)
            .addToBackStack(null)
            .commit()
    }
}

/**
 * Initialize listener to launch settings dialog
 *
 * @param fragment [Fragment]: calling fragment
 * @param settings [Settings]: object containing current settings
 * @param viewToClick [View]: view which should launch settings dialog when clicked
 */
fun initSettingsDialog(
    fragment: Fragment,
    settings: Settings,
    viewToClick: View
) {
    val context = fragment.requireContext()
    val settingsDialog = SettingsDialog()

    viewToClick.setOnClickListener {
        addArgsToFragment(context, settings, settingsDialog)
        settingsDialog.show(fragment.childFragmentManager, SettingsDialog.TAG)
    }
}

/**
 * Add settings arguments to fragment
 *
 * @param context [Context]: activity context, used to retrieve string keys
 * @param settings [Settings]: object containing current settings
 * @param fragment [Fragment]: fragment to add arguments to
 */
private fun addArgsToFragment(context: Context, settings: Settings, fragment: Fragment) {
    val numbersKey = context.getString(R.string.key_shuffle_numbers)
    val operatorsKey = context.getString(R.string.key_shuffle_operators)
    val parensKey = context.getString(R.string.key_apply_parens)
    val clearOnErrorKey = context.getString(R.string.key_clear_on_error)
    val decimalsKey = context.getString(R.string.key_apply_decimals)
    val settingsButtonKey = context.getString(R.string.key_settings_button)
    val mainFragmentKey = context.getString(R.string.key_main_fragment)
    val historyRandomnessKey = context.getString(R.string.key_random_history)

    fragment.arguments = bundleOf(
        numbersKey to settings.shuffleNumbers,
        operatorsKey to settings.shuffleOperators,
        parensKey to settings.applyParens,
        clearOnErrorKey to settings.clearOnError,
        decimalsKey to settings.applyDecimals,
        settingsButtonKey to settings.showSettingsButton,
        mainFragmentKey to (fragment is MainFragment),
        historyRandomnessKey to settings.historyRandomness
    )
}
