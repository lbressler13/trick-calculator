package com.example.trickcalculator.ui

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

    /**
     * Re-add action bar settings when fragment is shown.
     */
    override fun onResume() {
        super.onResume()
        runSetup()
    }

    /**
     * Run initial setup of action bar.
     */
    private fun runSetup() {
        val actionBar = requireMainActivity().binding.actionBar

        // set onClick
        if (setActionBarOnClick == null) {
            actionBar.root.setOnClickListener(null)
        } else {
            setActionBarOnClick!!(actionBar.root)
        }

        // set title
        val title = requireContext().getString(titleResId)
        actionBar.title.text = title

        requireMainActivity().fragmentManager = childFragmentManager
    }

    /**
     * Get current activity as [MainActivity].
     *
     * @return [MainActivity]
     */
    fun requireMainActivity(): MainActivity = requireActivity() as MainActivity
}
