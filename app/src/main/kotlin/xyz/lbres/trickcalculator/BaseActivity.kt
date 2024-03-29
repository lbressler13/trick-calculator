package xyz.lbres.trickcalculator

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import xyz.lbres.trickcalculator.databinding.ActivityBaseBinding

/**
 * Activity that contains all functionality of app
 */
class BaseActivity : AppCompatActivity() {

    lateinit var binding: ActivityBaseBinding
    private var isDarkMode = true

    /**
     * Initialize activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaseBinding.inflate(layoutInflater)
        ProductFlavor.setupFlavor(this)

        setContentView(binding.root)
        isDarkMode = getSystemDarkMode()
    }

    /**
     * Pop most recent fragment from backstack.
     */
    fun popBackStack() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.popBackStack()
    }

    /**
     * Run navigation action.
     *
     * @param actionResId [Int]: resource ID of action to run
     * @param args [Bundle?]: arguments to pass with action
     */
    fun runNavAction(actionResId: Int, args: Bundle? = null) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController.navigate(actionResId, args)
    }

    /**
     * Perform updates when dark mode is toggled
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val newMode = getSystemDarkMode()
        if (newMode != isDarkMode) {
            recreate()
            isDarkMode = newMode
        }
    }

    /**
     * Determine if phone is set to dark mode
     */
    private fun getSystemDarkMode(): Boolean {
        val nightModeFlags: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
    }
}
