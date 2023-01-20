package xyz.lbres.trickcalculator.ui.devtools

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.databinding.DialogDeveloperToolsBinding
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.FragmentDevToolsContext
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel
import xyz.lbres.trickcalculator.utils.gone
import xyz.lbres.trickcalculator.utils.visible

/**
 * Dialog with various developer tools, only available in dev build flavor
 */
class DeveloperToolsDialog : DialogFragment() {
    private lateinit var binding: DialogDeveloperToolsBinding
    private lateinit var viewModel: SharedViewModel

    /**
     * Build dialog, comes before onCreateView and dialog is not connected to context
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeveloperToolsBinding.inflate(layoutInflater)
        val root = binding.root

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val doneText = requireContext().getString(R.string.done)
        val title = requireContext().getString(R.string.title_dev_tools)

        return AlertDialog.Builder(requireContext())
            .setView(root)
            .setMessage(title)
            .setPositiveButton(doneText) { _, _ -> }
            .create()
    }

    /**
     * Continue initialization after view is connected to context
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.clearHistoryButton.setOnClickListener {
            val parentContext = FragmentDevToolsContext.currentContext
            viewModel.clearHistory()
            parentContext?.handleHistoryCleared?.invoke()
        }

        binding.refreshUIButton.setOnClickListener { requireActivity().recreate() }

        initHideDevTools()
        initSettingsNavigation()

        return binding.root
    }

    /**
     * Initialize the spinner used to set the time to hide the dev tools button.
     */
    private fun initHideDevTools() {
        val spinner: Spinner = binding.devToolsTimeSpinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.dev_tools_time_options,
            R.layout.component_spinner_item_selected
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.component_spinner_item_dropdown)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        binding.hideDevToolsButton.setOnClickListener { hideDevToolsOnClick() }
    }

    /**
     * On click for the hide dev tools button.
     * Hides the button for an amount of time based on the current value of the spinner.
     */
    private fun hideDevToolsOnClick() {
        val timerString = binding.devToolsTimeSpinner.selectedItem.toString()
        val numString = timerString.substring(0, timerString.length - 2) // remove ms from end
        val timer = Integer.parseInt(numString).toLong()

        val button = (requireActivity() as BaseActivity).binding.devToolsButton
        button.gone()

        // unhide dev tools button
        Handler(Looper.getMainLooper()).postDelayed({
            button.visible()
        }, timer)

        dismiss()
    }

    /**
     * Initialize navigation to settings fragment.
     */
    private fun initSettingsNavigation() {
        val fromDialogKey = getString(R.string.from_dialog_key)

        binding.openSettingsButton.setOnClickListener {
            val baseActivity = requireActivity() as BaseActivity
            val baseFragment = requireParentFragment() as BaseFragment

            if (baseFragment.navigateToSettings != null) {
                val args = bundleOf(fromDialogKey to true)
                baseActivity.runNavAction(baseFragment.navigateToSettings!!, args)
            }
            dismiss()
        }
    }

    companion object {
        // tag is required when showing fragment
        const val TAG = "DeveloperToolsDialog"
    }
}
