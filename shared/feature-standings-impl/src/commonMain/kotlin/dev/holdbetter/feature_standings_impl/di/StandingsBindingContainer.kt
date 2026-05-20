package dev.holdbetter.feature_standings_impl.di

import dev.holdbetter.feature_standings_api.StandingsRepository
import dev.holdbetter.feature_standings_api.StandingsStore
import dev.holdbetter.feature_standings_impl.data.StandingsDataSource
import dev.holdbetter.feature_standings_impl.data.StandingsDataSourceImpl
import dev.holdbetter.feature_standings_impl.data.StandingsRepositoryImpl
import dev.holdbetter.feature_standings_impl.domain.StandingsStoreImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
@BindingContainer
abstract class StandingsBindingContainer {
    @Binds
    internal abstract val StandingsStoreImpl.bind: StandingsStore

    @Binds
    internal abstract val StandingsRepositoryImpl.bind: StandingsRepository

    @Binds
    internal abstract val StandingsDataSourceImpl.bind: StandingsDataSource
}