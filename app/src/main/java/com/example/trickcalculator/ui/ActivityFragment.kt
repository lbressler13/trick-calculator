package com.example.trickcalculator.ui

import android.view.View
import androidx.fragment.app.Fragment
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R

/**
 * Abstract fragment to run initialization code that is required for every fragment in the app
 */
abstract class ActivityFragment : Fragment() {
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
        val mainActivity = requireActivity() as MainActivity
        val actionBar = mainActivity.binding.actionBar

        // set onclick
        if (setActionBarOnClick == null) {
            actionBar.root.setOnClickListener(null)
        } else {
            setActionBarOnClick!!(actionBar.root)
        }

        // set title
        val title = requireContext().getString(titleResId)
        actionBar.title.text = title

        mainActivity.fragmentManager = childFragmentManager
    }
}
