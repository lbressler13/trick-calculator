package xyz.lbres.trickcalculator

/**
 * Configuration for final build flavor
 */
object BuildFlavor : BuildFlavorConfig {
    override val devMode = false
    override fun setupFlavor(activity: MainActivity) {}
}
