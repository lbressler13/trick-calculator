package com.example.trickcalculator.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R

/**
 * Abstract fragment to handle common functionality involving the MainActivity
 */
abstract class BaseFragment : NavHostFragment() {
    /**
     * Resource ID for the title in the action bar.
     * Default is the ID for "Calculator"
     */
    protected open var titleResId: Int = R.string.title_action_bar

    /**
     * Function to set the onClick for the action bar. If null, the onClick is set to null.
     * Otherwise, function is called the action bar as an argument.
     */
    protected open var setActionBarOnClick: ((View) -> Unit)? = null

    override fun onResume() {
        super.onResume()
        runSetup()
    }

    private fun runSetup() {
        val actionBar = getMainActivity().binding.actionBar

        // set onClick
        if (setActionBarOnClick == null) {
            actionBar.root.setOnClickListener(null)
        } else {
            setActionBarOnClick!!(actionBar.root)
        }

        // set title
        val title = requireContext().getString(titleResId)
        actionBar.title.text = title

        getMainActivity().fragmentManager = childFragmentManager
    }

    fun runNavAction(actionResId: Int, args: Bundle? = null) {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (args == null) {
            navController.navigate(actionResId)
        } else {
            navController.navigate(actionResId, args)
        }
    }

    fun popBackStack() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.popBackStack()
    }

    fun getMainActivity(): MainActivity = requireActivity() as MainActivity
}
