package xyz.lbres.trickcalculator.ui.attributions.constants

import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.ui.attributions.AuthorAttribution
import xyz.lbres.trickcalculator.ui.attributions.ImageAttribution

/**
 * Constant lists of image attributions
 */

private val freepikImages = listOf(
    ImageAttribution(R.drawable.ic_chevron_down, R.string.chevron_down_cd, chevronDownUrl),
    ImageAttribution(R.drawable.ic_chevron_left, R.string.chevron_left_cd, chevronLeftUrl),
    ImageAttribution(R.drawable.ic_chevron_right, R.string.chevron_right_cd, chevronRightUrl),
    ImageAttribution(R.drawable.ic_chevron_up, R.string.chevron_up_cd, chevronUpUrl),
    ImageAttribution(R.drawable.ic_equals, R.string.equals_cd, equalsUrl),
    ImageAttribution(R.drawable.ic_info, R.string.info_cd, infoUrl),
    ImageAttribution(R.drawable.launcher, R.string.launcher_cd, launcherUrl),
    ImageAttribution(R.drawable.ic_minus, R.string.minus_cd, minusUrl),
    ImageAttribution(R.drawable.ic_plus, R.string.plus_cd, plusUrl),
    ImageAttribution(R.drawable.ic_settings, R.string.settings_cd, settingsUrl),
    ImageAttribution(R.drawable.ic_times, R.string.times_cd, timesUrl),
)

private val iconKananImages = listOf(
    ImageAttribution(R.drawable.ic_history, R.string.history_cd, historyUrl),
)

private val joalfaIcons = listOf(
    ImageAttribution(R.drawable.ic_download, R.string.last_history_cd, downloadUrl),
)

private val ilhamFitrotulHayatImages = listOf(
    ImageAttribution(R.drawable.ic_arrow_left, R.string.backspace_cd, arrowLeftUrl),
    ImageAttribution(R.drawable.ic_close, R.string.close_cd, closeUrl),
)

private val smashiconsImages = listOf(
    ImageAttribution(R.drawable.ic_divide, R.string.divide_cd, divideUrl),
)

/**
 * Constant list of author attributions, including the above image attributions
 */

val authorAttributions = listOf(
    AuthorAttribution("Freepik", freepikUrl, freepikImages),
    AuthorAttribution("joalfa", joalfaUrl, joalfaIcons),
    AuthorAttribution("IconKanan", iconKananUrl, iconKananImages),
    AuthorAttribution("Ilham Fitrotul Hayat", ilhamFitrotulHayatUrl, ilhamFitrotulHayatImages),
    AuthorAttribution("Smashicons", smashiconsUrl, smashiconsImages),
)
