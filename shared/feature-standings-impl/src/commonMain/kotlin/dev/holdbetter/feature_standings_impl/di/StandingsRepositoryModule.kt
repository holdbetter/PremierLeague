package dev.holdbetter.feature_standings_impl.di

import dev.holdbetter.core_network.di.NetworkModule
import dev.holdbetter.feature_standings_api.StandingsRepository
import dev.holdbetter.feature_standings_impl.data.StandingsDataSource
import dev.holdbetter.feature_standings_impl.data.StandingsDataSourceImpl
import dev.holdbetter.feature_standings_impl.data.StandingsRepositoryImpl
import dev.shustoff.dikt.Create
import dev.shustoff.dikt.CreateSingle
import dev.shustoff.dikt.UseModules

@UseModules(NetworkModule::class)
internal class StandingsRepositoryModule(
    private val networkModule: NetworkModule
) {
    val repository: StandingsRepository by lazy(::getRepository)

    private val dataSource: StandingsDataSource by lazy(::getDataSource)

    @CreateSingle
    private fun getDataSource(): StandingsDataSourceImpl

    @Create
    private fun getRepository(): StandingsRepositoryImpl
}