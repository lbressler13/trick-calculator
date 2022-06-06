package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.StringList

/**
 * Determine if a given string is an operator
 *
 * @param element [String]: value to check
 * @param ops [List]: list of operators to check against
 * @return true is the element is a member of the ops list, false otherwise
 */
fun isOperator(element: String, ops: StringList): Boolean = element in ops
