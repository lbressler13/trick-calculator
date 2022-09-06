package com.example.trickcalculator

import BuildOptions
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.trickcalculator.databinding.ActivityMainBinding
import com.example.trickcalculator.ui.main.DeveloperToolsDialog
import com.example.trickcalculator.ui.main.MainFragment
import com.example.trickcalculator.ui.shared.SharedViewModel

/**
 * Activity that contains all functionality of app
 */
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var isDarkMode = true
    private lateinit var sharedViewModel: SharedViewModel

    // fragment manager used to show/hide dev tools dialog, set by the current fragment
    var fragmentManager: FragmentManager? = null

    /**
     * Initialize activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        initDevToolsDialog()

        setContentView(binding.root)
        isDarkMode = isDarkMode()
    }

    /**
     * Perform updates when dark mode is toggled
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val wasDarkMode = isDarkMode
        isDarkMode = isDarkMode()
        if (wasDarkMode != isDarkMode) {
            recreate()
        }
    }

    /**
     * Determine if phone is set to dark mode
     */
    private fun isDarkMode(): Boolean {
        val nightModeFlags: Int =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
    }

    /**
     * Show or hide the dev tools button, and set the on click for it
     */
    private fun initDevToolsDialog() {
        binding.devToolsButton.isVisible = BuildOptions.buildType == "dev"

        val dialog = DeveloperToolsDialog()
        binding.devToolsButton.setOnClickListener {
            if (fragmentManager != null) {
                dialog.show(fragmentManager!!, DeveloperToolsDialog.TAG)
            }
        }
    }
}
