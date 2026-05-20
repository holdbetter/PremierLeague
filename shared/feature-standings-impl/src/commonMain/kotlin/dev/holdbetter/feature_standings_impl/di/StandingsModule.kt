package dev.holdbetter.feature_standings_impl.di

import dev.holdbetter.feature_standings_api.StandingsStore
import dev.holdbetter.shared.core_database.api.DatabaseApi
import dev.holdbetter.shared.core_navigation.RouterProvider
import dev.zacsweers.metro.Inject

@Inject
class StandingsModule(
    val routerProvider: RouterProvider,
    val databaseApi: DatabaseApi,
    val store: StandingsStore
)