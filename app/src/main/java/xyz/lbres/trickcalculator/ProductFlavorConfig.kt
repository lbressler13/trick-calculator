package xyz.lbres.trickcalculator

/**
 * Configuration interface that must be implemented in each product flavor.
 * Created to ensure consistency across flavors.
 */
interface ProductFlavorConfig {
    /**
     * If current flavor is dev
     */
    val devMode: Boolean

    /**
     * Run any setup necessary for the flavor
     * @param activity [BaseActivity]: initial activity
     */
    fun setupFlavor(activity: BaseActivity)
}
