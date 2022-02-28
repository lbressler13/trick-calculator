package com.example.trickcalculator.ext

import android.view.ViewGroup
import androidx.core.view.children

fun ViewGroup.disableAllChildren() {
    children.forEach { it.disable() }
}

fun ViewGroup.enableAllChildren() {
    children.forEach { it.enable() }
}
