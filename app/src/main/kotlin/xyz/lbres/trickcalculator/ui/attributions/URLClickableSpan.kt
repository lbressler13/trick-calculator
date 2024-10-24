package xyz.lbres.trickcalculator.ui.attributions

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import xyz.lbres.trickcalculator.utils.AppLogger

/**
 * Implementation of ClickableSpan, where the clicked text opens a link
 *
 * @param url [String]: the url to open when text is clicked
 */
class URLClickableSpan(private val url: String) : ClickableSpan() {
    /**
     * Open the specified url in a browser window
     */
    override fun onClick(view: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        ContextCompat.startActivity(view.context, browserIntent, null)
    }

    companion object {
        /**
         * Add URLClickableSpan to a string
         *
         * @param text [SpannableString]: the string to add a span to
         * @param url [String]: the url to open when the string is clicked
         */
        fun addToText(
            text: SpannableString,
            url: String,
        ) {
            text.setSpan(URLClickableSpan(url), 0, text.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }

        /**
         * Add URLClickableSpan to the first occurrence of a word in a string
         *
         * @param text [SpannableString]: the string to add a span to
         * @param word [String]: the word to be made clickable
         * @param url [String]: the url to open when the word is clicked
         */
        fun addToFirstOccurrence(
            text: SpannableString,
            word: String,
            url: String,
        ) {
            try {
                val start = text.indexOf(word)
                val end = start + word.length
                text.setSpan(URLClickableSpan(url), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            } catch (e: IndexOutOfBoundsException) {
                AppLogger.e(null, "Failed to add url to word: $word. Exception: $e.")
            }
        }
    }
}
