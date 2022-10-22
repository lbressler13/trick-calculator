package xyz.lbres.trickcalculator

object BuildFlavor : BuildFlavorConfig {
    override val devMode = false

    override fun setupFlavor(activity: MainActivity) {}
}
