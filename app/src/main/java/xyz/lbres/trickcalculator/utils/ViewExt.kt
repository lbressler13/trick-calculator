package xyz.lbres.trickcalculator.utils

import android.view.View

// TODO espresso tests for these functions

/**
 * Enable view
 */
fun View.enable() {
    isEnabled = true
}

/**
 * Disable view
 */
fun View.disable() {
    isEnabled = false
}

/**
 * Make view visible
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Make view invisible
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Make view gone
 */
fun View.gone() {
    visibility = View.GONE
}
