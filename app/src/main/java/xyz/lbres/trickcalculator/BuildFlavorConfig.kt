package xyz.lbres.trickcalculator

/**
 * Configuration interface that must be implemented in each build flavor.
 * Created to ensure consistency across flavors.
 */
interface BuildFlavorConfig {
    /**
     * If current flavor is dev
     */
    val devMode: Boolean

    /**
     * Run any setup necessary for the flavor
     * @param activity [MainActivity]: initial activity
     */
    fun setupFlavor(activity: MainActivity)
}
