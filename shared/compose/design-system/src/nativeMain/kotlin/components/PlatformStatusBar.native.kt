package dev.holdbetter.compose.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun PlatformStatusBar(color: Color) {
    Spacer(modifier = Modifier.fillMaxWidth()
        .windowInsetsTopHeight(WindowInsets.statusBars)
        .background(color)
    )
}