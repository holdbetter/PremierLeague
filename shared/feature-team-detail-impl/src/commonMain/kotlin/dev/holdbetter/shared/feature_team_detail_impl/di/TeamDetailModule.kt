package dev.holdbetter.shared.feature_team_detail_impl.di

import dev.holdbetter.shared.core_navigation.Router
import dev.holdbetter.shared.core_navigation.di.NavigationModule
import dev.holdbetter.shared.feature_team_detail.TeamDetailStore
import dev.holdbetter.shared.feature_team_detail_impl.domain.TeamDetailStoreImpl
import dev.shustoff.dikt.ProvidesMembers
import dev.shustoff.dikt.resolve

internal class TeamDetailModule(
    teamId: Long,
    private val navigationModule: NavigationModule,
    @ProvidesMembers private val teamDetailRepositoryModule: TeamDetailRepositoryModule
) {

    val store: TeamDetailStore by lazy { getStore(teamId) }

    val router: Router
        get() = navigationModule.router

    private fun getStore(teamId: Long): TeamDetailStoreImpl = resolve()
}