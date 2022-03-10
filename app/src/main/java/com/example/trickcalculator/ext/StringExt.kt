package com.example.trickcalculator.ext

/**
 * Substring function which uses end index instead of start index
 *
 * @param index [Int]: end index of substring
 * @return substring of this, starting at 0 and ending at current index
 * @throws IndexOutOfBoundsException if index is greater than size or less than 0
 */
fun String.substringTo(index: Int): String = substring(0, index)
