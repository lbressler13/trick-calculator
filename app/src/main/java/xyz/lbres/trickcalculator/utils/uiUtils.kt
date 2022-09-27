package xyz.lbres.trickcalculator.utils

import android.content.Context
import android.content.res.ColorStateList
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import androidx.core.widget.ImageViewCompat
import xyz.lbres.trickcalculator.R

/**
 * Underline an entire piece of text
 *
 * @param text [String]: text to underline
 * @return [SpannableString]: the underlined text
 */
fun createUnderlineText(text: String): SpannableString {
    val spannableString = SpannableString(text)
    spannableString.setSpan(UnderlineSpan(), 0, text.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    return spannableString
}

/**
 * Set the tint color on [button], if it is an [ImageButton].
 *
 * @param button [View]
 * @param color [Int]
 */
fun setImageButtonTint(button: View, color: Int) {
    if (button is ImageButton) {
        ImageViewCompat.setImageTintList(button, ColorStateList.valueOf(color))
    }
}

/**
 * Get theme colorOnPrimary color value.
 *
 * @param context [Context]
 * @return [Int]: the color value
 */
fun getColorOnPrimary(context: Context): Int {
    val color = TypedValue()
    context.theme.resolveAttribute(R.attr.colorOnPrimary, color, true)
    return color.data
}

/**
 * Get theme disabledForeground color value.
 *
 * @param context [Context]
 * @return [Int]: the color value
 */
fun getDisabledForeground(context: Context): Int {
    val color = TypedValue()
    context.theme.resolveAttribute(R.attr.disabledForeground, color, true)
    return color.data
}
