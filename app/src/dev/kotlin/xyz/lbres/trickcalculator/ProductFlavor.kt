package xyz.lbres.trickcalculator

import xyz.lbres.trickcalculator.ui.devtools.DeveloperToolsDialog
import xyz.lbres.trickcalculator.utils.visible

/**
 * Configuration for dev build flavor
 */
object ProductFlavor : ProductFlavorConfig {
    override val devMode = true

    /**
     * Show dev tools button and setup dialog
     */
    override fun setupFlavor(activity: BaseActivity) {
        val devToolsButton = activity.binding.devToolsButton

        devToolsButton.visible()

        val dialog = DeveloperToolsDialog()
        devToolsButton.setOnClickListener {
            val fragmentContext = activity.fragmentContext

            if (fragmentContext != null) {
                val requestKey = activity.getString(R.string.dev_tools_key)
                val fragmentManager = fragmentContext.childFragmentManager

                fragmentManager.setFragmentResultListener(requestKey, fragmentContext.lifecycleOwner) { _, _ ->
                    fragmentContext.handleSettingsChange()
                }

                dialog.show(fragmentManager, DeveloperToolsDialog.TAG)
            }
        }
    }
}
