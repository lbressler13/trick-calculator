package com.example.trickcalculator

import BuildOptions
import android.content.res.ColorStateList
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.trickcalculator.databinding.ActivityMainBinding
import com.example.trickcalculator.ext.disable
import com.example.trickcalculator.ext.enable
import com.example.trickcalculator.ext.gone
import com.example.trickcalculator.ext.visible
import com.example.trickcalculator.ui.main.MainFragment
import com.example.trickcalculator.ui.shared.SharedViewModel

/**
 * Activity that contains all functionality of app
 */
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var isDarkMode = true
    private lateinit var sharedViewModel: SharedViewModel

    /**
     * Initialize activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        assignTheme()
        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        setContentView(binding.root)
        initDevModeSwitch()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val wasDarkMode = isDarkMode
        assignTheme()
        if (wasDarkMode != isDarkMode) {
            recreate()
        }
    }

    /**
     * Assign theme based on night mode
     */
    private fun assignTheme() {
        val nightModeFlags: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                setTheme(R.style.CustomDark)
                isDarkMode = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                setTheme(R.style.CustomLight)
                isDarkMode = false
            }
            else -> {
                setTheme(R.style.CustomDark)
                isDarkMode = true
            }
        }
   }

    /**
     * Initialize dev mode switch based on build type and theme
     */
    private fun initDevModeSwitch() {
        val switch: SwitchCompat = binding.actionBar.devModeSwitch

        if (BuildOptions.buildType == "dev") {
            val checkedColor = TypedValue()
            theme.resolveAttribute(R.attr.actionBarSwitchTrackCheckedColor, checkedColor, true)
            val uncheckedColor = TypedValue()
            theme.resolveAttribute(R.attr.actionBarSwitchTrackUncheckedColor, uncheckedColor, true)

            val buttonStates = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf()
                ), intArrayOf(
                    uncheckedColor.data,
                    checkedColor.data,
                    uncheckedColor.data
                )
            )
            switch.trackDrawable.setTintList(buttonStates)

            switch.isChecked = true
            sharedViewModel.setIsDevMode(true)
            switch.setOnCheckedChangeListener { _, isChecked -> sharedViewModel.setIsDevMode(isChecked) }

            switch.enable()
            switch.visible()
        } else {
            switch.isChecked = false
            switch.disable()
            switch.gone()
        }
    }
}