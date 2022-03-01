package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.StringList

fun isOperator(element: String, ops: StringList): Boolean {
    return element in ops
}
