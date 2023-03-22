package dev.holdbetter.assets

import android.content.Context
import android.content.res.Configuration

typealias assetsColor = R.color
typealias assetsDrawable = R.drawable
typealias assetsDimen = R.dimen
typealias assetsFont = R.font

fun Context.isDarkMode(): Boolean {
    val darkModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
}