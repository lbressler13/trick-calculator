package com.example.trickcalculator.ui.attributions

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat

/**
 * Implementation of ClickableSpan, where the clicked text opens a link
 *
 * @param url [String]: the url to open when text is clicked
 */
class URLClickableSpan(private val url: String) : ClickableSpan() {
    /**
     * Open the specified url in a browser window
     */
    override fun onClick(widget: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        ContextCompat.startActivity(widget.context, browserIntent, null)
    }

    companion object {
        /**
         * Add URLClickableSpan to the first occurrence of a word in a string
         *
         * @param text [SpannableString]: the string to add a span to
         * @param word [String]: the word to be made clickable
         * @param url [String]: the url to open when the word is clicked
         * @throws IndexOutOfBoundsException if word is not in text
         */
        fun addToFirstWord(text: SpannableString, word: String, url: String) {
            val start = text.indexOf(word)
            val end = start + word.length
            text.setSpan(URLClickableSpan(url), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
    }
}
