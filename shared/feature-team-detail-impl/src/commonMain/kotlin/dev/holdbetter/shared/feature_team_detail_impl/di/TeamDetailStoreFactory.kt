package dev.holdbetter.shared.feature_team_detail_impl.di

import dev.holdbetter.shared.feature_team_detail.TeamDetailStore

interface TeamDetailStoreFactory {
    fun create(teamId: Long): TeamDetailStore
}