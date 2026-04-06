package dev.holdbetter.assets

import android.view.View

fun interface InsetsHandler {
    operator fun invoke(rootView: View, windowState: WindowState)
}