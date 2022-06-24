package com.example.trickcalculator.ui.attributions

import com.example.trickcalculator.R

data class AuthorAttribution(
    val name: String,
    val url: String,
    val images: List<ImageAttribution>
)

data class ImageAttribution(
    val iconResId: Int,
    val contentDescriptionId: Int,
    val url: String
)

private val freepikImages = listOf(
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
