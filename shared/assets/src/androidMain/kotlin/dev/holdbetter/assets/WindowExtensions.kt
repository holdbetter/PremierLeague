package dev.holdbetter.assets

import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_DEFAULT

fun WindowState.updateWindowView(
    hideNavigationBar: Boolean,
    isLightText: Boolean
) {
    with(window) {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // Run fullscreen and override optional systems insets (e.g Nothing OS Split Screen)
        decorView.systemUiVisibility =
            (decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

        val windowInsetsController = WindowCompat.getInsetsController(this, decorView)
        if (hideNavigationBar) {
            windowInsetsController.systemBarsBehavior = BEHAVIOR_DEFAULT
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
            isNavigationHidden = true
        } else {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            windowInsetsController.show(WindowInsetsCompat.Type.navigationBars())
        }

        windowInsetsController.isAppearanceLightStatusBars = !isLightText
        windowInsetsController.isAppearanceLightNavigationBars = !isLightText

        // Override listener from previous screen
        decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            view.onApplyWindowInsets(windowInsets)
        }
    }
}
