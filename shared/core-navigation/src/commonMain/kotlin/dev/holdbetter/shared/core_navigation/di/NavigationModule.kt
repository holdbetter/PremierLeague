package dev.holdbetter.shared.core_navigation.di

import dev.holdbetter.shared.core_navigation.NavigationController
import dev.holdbetter.shared.core_navigation.Router
import dev.holdbetter.shared.core_navigation.getRouter

class NavigationModule(
    navigationController: NavigationController
) {
    val router: Router = getRouter(navigationController)
}