package dev.holdbetter.assets

import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Window.updateColors(
    @ColorInt status: Int?,
    @ColorInt navigation: Int?,
    isLightText: Boolean
) {
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

    val windowInsetsController = WindowCompat.getInsetsController(this, decorView)

    if (status == null) {
        addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    } else {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    if (navigation == null) {
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    } else {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        windowInsetsController.show(WindowInsetsCompat.Type.navigationBars())
    }

    WindowInsetsControllerCompat(this, decorView).isAppearanceLightStatusBars = !isLightText
    WindowInsetsControllerCompat(this, decorView).isAppearanceLightNavigationBars = !isLightText

    status?.let { statusBarColor = status }
    navigation?.let { navigationBarColor = navigation }

    decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
        view.onApplyWindowInsets(windowInsets)
    }
}
