package com.example.trickcalculator.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat

fun isInt(value: String): Boolean {
    return try {
        Integer.parseInt(value)
        true
    } catch (e: Exception) {
        false
    }
}

fun setImageButtonTint(button: View, colorId: Int, context: Context) {
    if (button is ImageButton) {
        val color: Int = ContextCompat.getColor(context, colorId)
        ImageViewCompat.setImageTintList(button, ColorStateList.valueOf(color))
    }
}
