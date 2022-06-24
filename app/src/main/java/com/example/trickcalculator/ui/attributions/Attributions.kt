package com.example.trickcalculator.ui.attributions

import com.example.trickcalculator.R

data class AuthorAttribution(
    val name: String,
    val url: String,
    val images: List<ImageAttribution>
)

data class ImageAttribution(
    val iconRedId: Int,
    val url: String
)

private val freepikImages = listOf(
    ImageAttribution(
        R.drawable.ic_equals,
        "www.flaticon.com/free-icon/equal_56751"
    ),
    ImageAttribution(
        R.drawable.ic_info,
        "www.flaticon.com/free-icon/info-button_64494"
    ),
    ImageAttribution(
        R.drawable.ic_minus,
        "www.flaticon.com/free-icon/minus_56889"
    ),
    ImageAttribution(
        R.drawable.ic_plus,
        "www.flaticon.com/premium-icon/plus_3524388"
    ),
    ImageAttribution(
        R.drawable.ic_settings,
        "https://www.flaticon.com/premium-icon/gear_484613"
    ),
    ImageAttribution(
        R.drawable.ic_times,
        "www.flaticon.com/free-icon/multiply-mathematical-sign_43823"
    )
)

private val iconKananImages = listOf(
    ImageAttribution(
        R.drawable.ic_history,
        "www.flaticon.com/premium-icon/history_2901149"
    )
)

private val ilhamFitrotulHayatImages = listOf(
    ImageAttribution(
        R.drawable.ic_arrow_left,
        "www.flaticon.com/premium-icon/left_3416141"
    ),
    ImageAttribution(
        R.drawable.ic_close,
        "www.flaticon.com/premium-icon/cross_4421536"
    )
)

private val pixelPerfectImages = listOf(
    ImageAttribution(
        R.drawable.launcher,
        "https://www.flaticon.com/free-icon/keys_2891382"
    ),
)

private val smashiconsImages = listOf(
    ImageAttribution(
        R.drawable.ic_divide,
        "www.flaticon.com/free-icon/divide_149702"
    )
)

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

