package dev.holdbetter.feature_standings_impl.di

import dev.holdbetter.core_di_api.folder.Dikt
import dev.holdbetter.core_network.di.NetworkModule
import dev.holdbetter.feature_standings_api.StandingsRepository
import dev.holdbetter.feature_standings_impl.data.StandingsDataSource
import dev.holdbetter.feature_standings_impl.data.StandingsDataSourceImpl
import dev.holdbetter.feature_standings_impl.data.StandingsRepositoryImpl
import dev.shustoff.dikt.ModuleScopes
import dev.shustoff.dikt.ProvidesMembers
import dev.shustoff.dikt.resolve

@ModuleScopes(Dikt::class)
internal class StandingsRepositoryModule(
    @ProvidesMembers private val networkModule: NetworkModule
) {
    val repository: StandingsRepository by lazy(::getRepository)

    private val dataSource: StandingsDataSource by lazy(::getDataSource)

    private fun getDataSource(): StandingsDataSourceImpl = resolve()

    private fun getRepository(): StandingsRepositoryImpl = resolve()
}