package dev.holdbetter.shared.core_database.di

import android.content.Context
import dev.holdbetter.shared.core_database.api.DatabaseApi
import dev.holdbetter.shared.core_database.database.Database
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@DependencyGraph(AppScope::class)
abstract class AndroidDatabaseGraph : DatabaseGraph {

    @SingleIn(AppScope::class)
    @Provides
    fun getDatabaseApi(context: Context): DatabaseApi {
        return Database.getInstance(context)
    }

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides applicationContext: Context): AndroidDatabaseGraph
    }
}