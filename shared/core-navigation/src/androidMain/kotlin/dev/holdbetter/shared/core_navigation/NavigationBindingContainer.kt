package dev.holdbetter.shared.core_navigation

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
@BindingContainer
class NavigationBindingContainer {
    @Provides
    fun getRouterProvider(): RouterProvider {
        return {
            RouterImpl(it)
        }
    }
}