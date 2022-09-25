package xyz.lbres.trickcalculator.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.main.MainFragment
import xyz.lbres.trickcalculator.ui.settings.components.SettingsDialog

/**
 * Initialize onClick listener to launch settings fragment
 *
 * @param parentFragment [Fragment]: calling fragment
 * @param viewToClick [View]: view which should launch settings fragment when clicked
 * @param navResId [Int]: resource ID of action to navigate from [parentFragment] to settings fragment
 */
fun initSettingsFragment(parentFragment: Fragment, viewToClick: View, navResId: Int) {
    viewToClick.setOnClickListener {
        val activity = (parentFragment as BaseFragment).requireMainActivity()
        activity.runNavAction(navResId, getFragmentArgs(parentFragment))
    }
}

/**
 * Initialize onClick listener to launch settings dialog
 *
 * @param parentFragment [Fragment]: calling fragment
 * @param viewToClick [View]: view which should launch settings dialog when clicked
 */
fun initSettingsDialog(parentFragment: Fragment, viewToClick: View) {
    val settingsDialog = SettingsDialog()

    viewToClick.setOnClickListener {
        settingsDialog.arguments = getFragmentArgs(parentFragment)
        settingsDialog.show(parentFragment.childFragmentManager, SettingsDialog.TAG)
    }
}

/**
 * Modify the fragment args to indicate if the parent is a MainFragment
 *
 * @param parentFragment [Fragment]: calling fragment
 */
private fun getFragmentArgs(parentFragment: Fragment): Bundle {
    val context = parentFragment.requireContext()
    val mainFragmentKey = context.getString(R.string.key_main_fragment)

    return bundleOf(mainFragmentKey to (parentFragment is MainFragment))
}
