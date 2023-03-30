package dev.holdbetter.shared.core_database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.holdbetter.shared.core_database.api.DatabaseApi
import dev.holdbetter.shared.core_database.dao.FavoritesDao
import dev.holdbetter.shared.core_database.entity.UserFavoritesEntity

@Database(
    entities = [UserFavoritesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LeagueUserDatabase : RoomDatabase(), DatabaseApi {

    companion object {
        private const val DATABASE_NAME = "LeagueUserDatabase"

        @Volatile
        private var instance: LeagueUserDatabase? = null

        fun getInstance(context: Context): DatabaseApi {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): LeagueUserDatabase {
            return Room.databaseBuilder(
                context,
                LeagueUserDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }

    abstract fun favoritesDao(): FavoritesDao

    override fun favoritesApi() = favoritesDao()
}