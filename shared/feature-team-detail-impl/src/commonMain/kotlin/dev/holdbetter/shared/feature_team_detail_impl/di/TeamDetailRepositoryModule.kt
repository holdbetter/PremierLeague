package dev.holdbetter.shared.feature_team_detail_impl.di

import dev.holdbetter.core_di_api.folder.Dikt
import dev.holdbetter.core_network.di.NetworkModule
import dev.holdbetter.shared.core_database.di.DatabaseModule
import dev.holdbetter.shared.feature_team_detail.TeamDetailRepository
import dev.holdbetter.shared.feature_team_detail_impl.data.TeamDetailDataSource
import dev.holdbetter.shared.feature_team_detail_impl.data.TeamDetailDataSourceImpl
import dev.holdbetter.shared.feature_team_detail_impl.data.TeamDetailRepositoryImpl
import dev.shustoff.dikt.ModuleScopes
import dev.shustoff.dikt.ProvidesMembers
import dev.shustoff.dikt.resolve

@ModuleScopes(Dikt::class)
internal class TeamDetailRepositoryModule(
    @ProvidesMembers private val databaseModule: DatabaseModule,
    @ProvidesMembers private val networkModule: NetworkModule
) {

    private val dataSource: TeamDetailDataSource by lazy(::getDataSource)

    val repository: TeamDetailRepository by lazy(::getRepository)

    private fun getRepository(): TeamDetailRepositoryImpl = resolve()

    private fun getDataSource(): TeamDetailDataSourceImpl = resolve()
}