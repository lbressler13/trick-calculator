package com.example.trickcalculator.ui.attributions

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat

class UrlClickableSpan(private val url: String) : ClickableSpan() {
    // open link
    override fun onClick(widget: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        ContextCompat.startActivity(widget.context, browserIntent, null)
    }

    companion object {
        fun addToFirstWord(text: SpannableString, word: String, url: String) {
            val start = text.indexOf(word)
            if (start >= 0) {
                val end = start + word.length
                text.setSpan(UrlClickableSpan(url), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }
        }
    }
}
