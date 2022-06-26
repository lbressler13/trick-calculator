package com.example.trickcalculator.ui.settings

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.trickcalculator.R
import com.example.trickcalculator.ui.main.MainFragment
import com.example.trickcalculator.ui.settings.components.SettingsDialog
import com.example.trickcalculator.ui.settings.components.SettingsFragment

/**
 * Initialize onClick listener to launch settings fragment
 *
 * @param parentFragment [Fragment]: calling fragment
 * @param viewToClick [View]: view which should launch settings fragment when clicked
 */
fun initSettingsFragment(parentFragment: Fragment, viewToClick: View) {
    viewToClick.setOnClickListener {
        val newFragment = SettingsFragment.newInstance()
        setIsMainFragment(newFragment, parentFragment)

        parentFragment.requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, newFragment)
            .addToBackStack(null)
            .commit()
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
        setIsMainFragment(settingsDialog, parentFragment)
        settingsDialog.show(parentFragment.childFragmentManager, SettingsDialog.TAG)
    }
}

/**
 * Modify the fragment args to indicate if the parent is a MainFragment
 *
 * @param fragment [Fragment]: fragment to add args to
 * @param parentFragment [Fragment]: calling fragment
 */
private fun setIsMainFragment(fragment: Fragment, parentFragment: Fragment) {
    val context = parentFragment.requireContext()
    val mainFragmentKey = context.getString(R.string.key_main_fragment)

    fragment.arguments = bundleOf(mainFragmentKey to (parentFragment is MainFragment))
}
