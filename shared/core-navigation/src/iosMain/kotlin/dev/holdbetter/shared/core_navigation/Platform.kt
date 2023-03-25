package dev.holdbetter.shared.core_navigation

import platform.UIKit.UINavigationController

actual typealias NavigationController = UINavigationController

actual typealias Deeplink = String

actual fun getRouter(navigationController: NavigationController): Router = TODO()