package xyz.lbres.trickcalculator

/**
 * Configuration for final build flavor
 */
object ProductFlavor : ProductFlavorConfig {
    override val devMode = false
    override fun setupFlavor(activity: MainActivity) {}
}
