package dev.holdbetter.feature_standings_impl.di

import dev.holdbetter.core_di_api.folder.InjectorScope
import dev.holdbetter.feature_standings_impl.StandingsFragment
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ClassKey
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.MembersInjector

@ContributesTo(InjectorScope::class)
@BindingContainer
interface StandingsInjector {
    @Binds
    @IntoMap
    @ClassKey(StandingsFragment::class)
    val MembersInjector<StandingsFragment>.bind: MembersInjector<*>
}