package com.example.trickcalculator

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trickcalculator.databinding.ActivityMainBinding
import com.example.trickcalculator.ui.main.MainFragment

/**
 * Activity that contains all functionality of app
 */
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    /**
     * Initialize activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        assignTheme()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment.newInstance())
                .commitNow()
        }
    }

    /**
     * Assign theme based on night mode
     */
    private fun assignTheme() {
        val nightModeFlags: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> setTheme(R.style.CustomDark)
            Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.CustomLight)
            else -> setTheme(R.style.CustomDark)
        }
   }
}