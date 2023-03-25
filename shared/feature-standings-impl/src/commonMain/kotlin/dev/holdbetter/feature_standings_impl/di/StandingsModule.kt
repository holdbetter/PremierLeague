package dev.holdbetter.feature_standings_impl.di

import dev.holdbetter.feature_standings_api.StandingsStore
import dev.holdbetter.feature_standings_impl.domain.StandingsStoreImpl
import dev.holdbetter.shared.core_navigation.Router
import dev.holdbetter.shared.core_navigation.di.NavigationModule
import dev.shustoff.dikt.Create
import dev.shustoff.dikt.UseModules

@UseModules(StandingsRepositoryModule::class)
internal class StandingsModule(
    private val navigationModule: NavigationModule,
    private val standingsRepositoryModule: StandingsRepositoryModule
) {

    val store: StandingsStore by lazy(::getStore)

    val router: Router
        get() = navigationModule.router

    @Create
    private fun getStore(): StandingsStoreImpl
}