package xyz.lbres.trickcalculator.ext.string

import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan

/**
 * Create an underlined version of the string
 *
 * @return [SpannableString]: the underlined string
 */
fun String.underlined(): SpannableString {
    return SpannableString(this).apply {
        setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
}
