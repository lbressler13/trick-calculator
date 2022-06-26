package com.example.trickcalculator.ui.attributions

/**
 * Attribution information for an author
 *
 * @param name [String]: the author's display name in Flaticon
 * @param url [String]: link to author's page on Flaticon
 * @param images [List<ImageAttribution>]: list of images created by the author
 */
data class AuthorAttribution(
    val name: String,
    val url: String,
    val images: List<ImageAttribution>
)

/**
 * Attribution information for an image
 *
 * @param iconResId [Int]: resource id for drawable of icon
 * @param contentDescriptionId [Int]: resource id for string of content description for icon
 * @param url [String]: link to image on Flaticon
 */
data class ImageAttribution(
    val iconResId: Int,
    val contentDescriptionId: Int,
    val url: String
)
