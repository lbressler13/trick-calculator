package com.example.trickcalculator.ui

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R

abstract class ActivityFragment : Fragment() {
    protected open var titleResId: Int = R.string.title_action_bar
    open var setActionBarOnClick: ((View) -> Unit)? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        runSetup()
    }

    override fun onResume() {
        super.onResume()
        runSetup()
    }

    private fun runSetup() {
        val mainActivity = requireActivity() as MainActivity

        val actionBar = mainActivity.binding.actionBar

        if (setActionBarOnClick == null) {
            actionBar.root.setOnClickListener(null)
        } else {
            setActionBarOnClick!!(actionBar.root)
        }

        val title = requireContext().getString(titleResId)
        actionBar.title.text = title

        mainActivity.fragmentManager = childFragmentManager
    }
}
