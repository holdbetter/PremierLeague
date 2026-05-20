package dev.holdbetter.shared.feature_team_detail_impl.di

import dev.holdbetter.shared.core_navigation.RouterProvider
import dev.zacsweers.metro.Inject

@Inject
class TeamDetailModule(
    val routerProvider: RouterProvider,
    val teamDetailStoreFactory: TeamDetailStoreFactory
)