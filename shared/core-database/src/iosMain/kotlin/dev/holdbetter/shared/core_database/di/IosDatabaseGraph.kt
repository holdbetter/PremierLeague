package dev.holdbetter.shared.core_database.di

import dev.holdbetter.shared.core_database.api.DatabaseApi
import dev.holdbetter.shared.core_database.database.Database
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@DependencyGraph(AppScope::class)
abstract class IosDatabaseGraph : DatabaseGraph {

    @SingleIn(AppScope::class)
    @Provides
    fun getDatabaseApi(): DatabaseApi {
        return Database.getInstance()
    }
}