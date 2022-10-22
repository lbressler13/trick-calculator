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
    override fun setupFlavor(activity: MainActivity) {
        val devToolsButton = activity.binding.devToolsButton

        devToolsButton.visible()

        val dialog = DeveloperToolsDialog()
        devToolsButton.setOnClickListener {
            val fragmentManager = activity.fragmentManager

            if (fragmentManager != null) {
                dialog.show(fragmentManager, DeveloperToolsDialog.TAG)
            }
        }
    }
}
