package xyz.lbres.trickcalculator

interface BuildFlavorConfig {
    val devMode: Boolean

    fun setupFlavor(activity: MainActivity)
}
