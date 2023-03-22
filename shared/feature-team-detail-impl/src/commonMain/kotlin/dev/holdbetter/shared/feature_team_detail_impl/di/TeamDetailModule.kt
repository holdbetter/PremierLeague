package dev.holdbetter.shared.feature_team_detail_impl.di

import dev.holdbetter.shared.feature_team_detail.TeamDetailStore
import dev.holdbetter.shared.feature_team_detail_impl.domain.TeamDetailStoreImpl
import dev.shustoff.dikt.Create
import dev.shustoff.dikt.UseModules

@UseModules(TeamDetailRepositoryModule::class)
internal class TeamDetailModule(
    teamId: Long,
    private val teamDetailRepositoryModule: TeamDetailRepositoryModule
) {

    val store: TeamDetailStore by lazy { getStore(teamId) }

    @Create
    private fun getStore(teamId: Long): TeamDetailStoreImpl
}