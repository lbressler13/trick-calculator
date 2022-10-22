package xyz.lbres.trickcalculator

import xyz.lbres.trickcalculator.ui.shared.DeveloperToolsDialog
import xyz.lbres.trickcalculator.utils.visible

object BuildFlavor : BuildFlavorConfig {
    override val devMode = true

    /**
     * Show or hide the dev tools button, and set the on click for it
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
