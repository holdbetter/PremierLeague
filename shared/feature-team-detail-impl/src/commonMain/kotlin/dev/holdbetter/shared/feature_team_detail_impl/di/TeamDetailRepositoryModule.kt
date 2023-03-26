package dev.holdbetter.shared.feature_team_detail_impl.di

import dev.holdbetter.core_network.di.NetworkModule
import dev.holdbetter.shared.feature_team_detail.TeamDetailRepository
import dev.holdbetter.shared.feature_team_detail_impl.data.TeamDetailDataSource
import dev.holdbetter.shared.feature_team_detail_impl.data.TeamDetailDataSourceImpl
import dev.holdbetter.shared.feature_team_detail_impl.data.TeamDetailRepositoryImpl
import dev.shustoff.dikt.CreateSingle
import dev.shustoff.dikt.UseModules

@UseModules(NetworkModule::class)
internal class TeamDetailRepositoryModule(
    private val networkModule: NetworkModule
) {

    private val dataSource: TeamDetailDataSource by lazy(::getDataSource)

    val repository: TeamDetailRepository by lazy(::getRepository)

    @CreateSingle
    private fun getRepository(): TeamDetailRepositoryImpl

    @CreateSingle
    private fun getDataSource(): TeamDetailDataSourceImpl
}