package com.example.trickcalculator.ui.attributions

import com.example.trickcalculator.R

/**
 * Attribution information for an author
 *
 * @param name [String]: the author's display name in Flaticon
 * @param url [String]: link to author's page on Flaticon
 * @param images [List]: list of images created by the author
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

/**
 * Constant lists of image attributions
 */

private val freepikImages = listOf(
    ImageAttribution(
        R.drawable.ic_chevron_down,
        R.string.chevron_down_cd,
        "https://www.flaticon.com/premium-icon/down-chevron_1633716"
    ),
    ImageAttribution(
        R.drawable.ic_chevron_left,
        R.string.chevron_left_cd,
        "https://www.flaticon.com/premium-icon/left-chevron_1633718"
    ),
    ImageAttribution(
        R.drawable.ic_chevron_right,
        R.string.chevron_right_cd,
        "https://www.flaticon.com/premium-icon/right-chevron_1633719"
    ),
    ImageAttribution(
        R.drawable.ic_chevron_up,
        R.string.chevron_up_cd,
        "https://www.flaticon.com/premium-icon/up-chevron_1633717"
    ),
    ImageAttribution(
        R.drawable.ic_equals,
        R.string.equals_cd,
        "https://www.flaticon.com/free-icon/equal_56751"
    ),
    ImageAttribution(
        R.drawable.ic_info,
        R.string.info_cd,
        "https://www.flaticon.com/free-icon/info-button_64494"
    ),
    ImageAttribution(
        R.drawable.ic_minus,
        R.string.minus_cd,
        "https://www.flaticon.com/free-icon/minus_56889"
    ),
    ImageAttribution(
        R.drawable.ic_plus,
        R.string.plus_cd,
        "https://www.flaticon.com/premium-icon/plus_3524388"
    ),
    ImageAttribution(
        R.drawable.ic_settings,
        R.string.settings_cd,
        "https://www.flaticon.com/premium-icon/gear_484613"
    ),
    ImageAttribution(
        R.drawable.ic_times,
        R.string.times_cd,
        "https://www.flaticon.com/free-icon/multiply-mathematical-sign_43823"
    )
)

private val iconKananImages = listOf(
    ImageAttribution(
        R.drawable.ic_history,
        R.string.history_cd,
        "https://www.flaticon.com/premium-icon/history_2901149"
    )
)

private val ilhamFitrotulHayatImages = listOf(
    ImageAttribution(
        R.drawable.ic_arrow_left,
        R.string.backspace_cd,
        "https://www.flaticon.com/premium-icon/left_3416141"
    ),
    ImageAttribution(
        R.drawable.ic_close,
        R.string.close_cd,
        "https://www.flaticon.com/premium-icon/cross_4421536"
    )
)

private val pixelPerfectImages = listOf(
    ImageAttribution(
        R.drawable.launcher,
        R.string.launcher_cd,
        "https://www.flaticon.com/free-icon/keys_2891382"
    ),
)

private val smashiconsImages = listOf(
    ImageAttribution(
        R.drawable.ic_divide,
        R.string.divide_cd,
        "https://www.flaticon.com/free-icon/divide_149702"
    )
)

/**
 * Constant list of author attributions, including the above image attributions
 */


// TODO calculator url is incorrect

val authorAttributions = listOf(
    AuthorAttribution(
        "Freepik",
        "https://www.flaticon.com/authors/freepik",
        freepikImages
    ),
    AuthorAttribution(
        "IconKanan",
        "https://www.flaticon.com/authors/iconkanan",
        iconKananImages
    ),
    AuthorAttribution(
        "Ilham Fitrotul Hayat",
        "https://www.flaticon.com/authors/ilham-fitrotul-hayat",
        ilhamFitrotulHayatImages
    ),
    AuthorAttribution(
        "Pixel perfect",
        "https://www.flaticon.com/authors/pixel-perfect",
        pixelPerfectImages
    ),
    AuthorAttribution(
        "Smashicons",
        "https://www.flaticon.com/authors/smashicons",
        smashiconsImages
    )
)
