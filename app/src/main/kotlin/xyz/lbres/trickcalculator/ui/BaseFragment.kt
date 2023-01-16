package xyz.lbres.trickcalculator.ui

import androidx.navigation.fragment.NavHostFragment
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.utils.History

/**
 * Abstract fragment to handle common functionality involving the BaseActivity
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
    protected open var actionBarOnClick: (() -> Unit)? = null

    /**
     * Resource ID for the action to navigate to the settings page from the current fragment
     */
    abstract var navigateToSettings: Int?
        protected set

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
        val actionBar = requireBaseActivity().binding.actionBar

        // set onClick
        if (actionBarOnClick == null) {
            actionBar.root.setOnClickListener(null)
        } else {
            actionBar.root.setOnClickListener { actionBarOnClick!!() }
        }

        // set title
        val title = requireContext().getString(titleResId)
        actionBar.title.text = title

        FragmentDevToolsContext.currentContext = FragmentDevToolsContext(
            childFragmentManager,
        ) { handleHistoryChange(it) }
    }

    protected open fun handleHistoryChange(previousHistory: History) {}

    fun closeFragment() = requireBaseActivity().popBackStack()

    /**
     * Get current activity as [BaseActivity].
     *
     * @return [BaseActivity]
     */
    fun requireBaseActivity(): BaseActivity = requireActivity() as BaseActivity
}
