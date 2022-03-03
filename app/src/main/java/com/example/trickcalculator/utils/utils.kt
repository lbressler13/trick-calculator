package com.example.trickcalculator.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import java.math.BigDecimal

fun isInt(value: String): Boolean {
    return try {
        Integer.parseInt(value)
        true
    } catch (e: Exception) {
        false
    }
}

fun isNumber(value: String): Boolean {
    return try {
        if (value.endsWith('.')) {
            false
        } else {
            BigDecimal(value)
            true
        }
    } catch (e: Exception) {
        false
    }
}

fun isPartialDecimal(value: String): Boolean {
    // remove negative sign if it exists
    val positiveString = if (value.startsWith('-') && value.length > 1) {
        value.substring(1)
    } else {
        value
    }

    return positiveString.isNotEmpty() && positiveString.all { it.isDigit() || it == '.' }
}

fun setImageButtonTint(button: View, colorId: Int, context: Context) {
    if (button is ImageButton) {
        val color: Int = ContextCompat.getColor(context, colorId)
        ImageViewCompat.setImageTintList(button, ColorStateList.valueOf(color))
    }
}
