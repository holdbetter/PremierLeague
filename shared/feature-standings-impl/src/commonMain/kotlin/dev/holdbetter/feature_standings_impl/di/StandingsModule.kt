package dev.holdbetter.feature_standings_impl.di

import dev.holdbetter.feature_standings_api.StandingsStore
import dev.holdbetter.feature_standings_impl.domain.StandingsStoreImpl
import dev.shustoff.dikt.Create
import dev.shustoff.dikt.UseModules

@UseModules(StandingsRepositoryModule::class)
internal class StandingsModule(
    private val standingsRepositoryModule: StandingsRepositoryModule
) {

    val store: StandingsStore by lazy(::getStore)

    @Create
    private fun getStore(): StandingsStoreImpl
}