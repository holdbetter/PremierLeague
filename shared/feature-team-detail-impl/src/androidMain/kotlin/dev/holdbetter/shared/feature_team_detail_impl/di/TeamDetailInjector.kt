package dev.holdbetter.shared.feature_team_detail_impl.di

import dev.holdbetter.core_di_api.folder.InjectorScope
import dev.holdbetter.shared.feature_team_detail_impl.TeamDetailFragment
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ClassKey
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.MembersInjector

@ContributesTo(InjectorScope::class)
@BindingContainer
interface TeamDetailInjector {
    @Binds
    @IntoMap
    @ClassKey(TeamDetailFragment::class)
    val MembersInjector<TeamDetailFragment>.bind: MembersInjector<*>
}