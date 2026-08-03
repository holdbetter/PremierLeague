package dev.holdbetter.shared.core_database.impl.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import dev.holdbetter.shared.core_database.api.DatabaseApi
import dev.holdbetter.shared.core_database.impl.dao.FavoritesDao
import dev.holdbetter.shared.core_database.impl.entity.UserFavoritesEntity

@Database(
    entities = [UserFavoritesEntity::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(LeagueDatabaseConstructor::class)
abstract class LeagueUserDatabase : RoomDatabase(), DatabaseApi {

    companion object {
        const val DATABASE_NAME = "LeagueUserDatabase"
    }

    abstract fun favoritesDao(): FavoritesDao

    override fun favoritesApi() = favoritesDao()
}

@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object LeagueDatabaseConstructor : RoomDatabaseConstructor<LeagueUserDatabase> {
    override fun initialize(): LeagueUserDatabase
}