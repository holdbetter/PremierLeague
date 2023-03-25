package dev.holdbetter.shared.core_navigation

import android.net.Uri
import androidx.navigation.NavController

actual typealias NavigationController = NavController

actual typealias Deeplink = Uri.Builder

internal actual fun getRouter(navigationController: NavigationController): Router =
    RouterImpl(navigationController)