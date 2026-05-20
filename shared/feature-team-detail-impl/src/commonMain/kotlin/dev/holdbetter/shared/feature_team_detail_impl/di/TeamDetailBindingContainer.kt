package dev.holdbetter.shared.feature_team_detail_impl.di

import dev.holdbetter.shared.feature_team_detail.TeamDetailRepository
import dev.holdbetter.shared.feature_team_detail_impl.data.TeamDetailDataSource
import dev.holdbetter.shared.feature_team_detail_impl.data.TeamDetailDataSourceImpl
import dev.holdbetter.shared.feature_team_detail_impl.data.TeamDetailRepositoryImpl
import dev.holdbetter.shared.feature_team_detail_impl.domain.TeamDetailStoreImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
@BindingContainer
abstract class TeamDetailBindingContainer {
    @Binds internal abstract val TeamDetailStoreImpl.Factory.bind : TeamDetailStoreFactory

    @Binds internal abstract val TeamDetailDataSourceImpl.bind : TeamDetailDataSource

    @Binds internal abstract val TeamDetailRepositoryImpl.bind : TeamDetailRepository
}