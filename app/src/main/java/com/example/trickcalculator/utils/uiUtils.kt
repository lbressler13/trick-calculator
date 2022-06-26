package com.example.trickcalculator.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan

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
