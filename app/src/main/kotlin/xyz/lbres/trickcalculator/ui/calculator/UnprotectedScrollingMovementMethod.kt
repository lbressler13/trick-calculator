package xyz.lbres.trickcalculator.ui.calculator

import android.text.Spannable
import android.text.method.ScrollingMovementMethod
import android.widget.TextView

/**
 * Extends ScrollingMovementMethod to expose the protected functionality
 * of scrolling to top and bottom
 */
class UnprotectedScrollingMovementMethod : ScrollingMovementMethod() {
    fun goToBottom(widget: TextView?, buffer: Spannable? = null): Boolean {
        return bottom(widget, buffer)
    }

    fun goToTop(widget: TextView?, buffer: Spannable? = null): Boolean {
        return top(widget, buffer)
    }
}
