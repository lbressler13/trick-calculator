package com.example.trickcalculator.ui.main

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.util.TypedValue
import android.widget.TextView
import com.example.trickcalculator.R
import com.example.trickcalculator.ui.borderspan.*
import com.example.trickcalculator.utils.StringList

fun addBorder(fullText: StringList, digitsPerLine: Int, context: Context, parent: TextView): SpannableString {
    val textColor = TypedValue()
    context.theme.resolveAttribute(R.attr.colorOnPrimary, textColor, true)

    val parentWidth = parent.width - parent.paddingStart - parent.paddingEnd

    val spannableString = SpannableString(fullText.joinToString(""))
    val firstTerm = fullText[0]

    if (firstTerm.isEmpty()) {
        return spannableString
    }

    val lines = firstTerm.chunked(digitsPerLine)

    when (lines.size) {
        1 -> {
            spannableString.setSpan(
                SingleBorderSpan(textColor.data, parentWidth),
                0,
                firstTerm.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
        2 -> {
            spannableString.setSpan(
                TopBorderSpan(textColor.data, parentWidth),
                0,
                lines[0].length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(
                BottomBorderSpan(textColor.data, parentWidth),
                lines[0].length,
                lines[0].length + lines[1].length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
        else -> {
            spannableString.setSpan(
                TopBorderSpan(textColor.data, parentWidth),
                0,
                lines[0].length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )

            var startIndex = lines[0].length
            for (i in 1 until lines.lastIndex) {
                val length = lines[i].length
                spannableString.setSpan(
                    MiddleBorderSpan(textColor.data, parentWidth),
                    startIndex,
                    startIndex + length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                startIndex += length
            }

            spannableString.setSpan(
                BottomBorderSpan(textColor.data, parentWidth),
                startIndex,
                startIndex + lines.last().length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
    }

    return spannableString
}
