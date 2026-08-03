package dev.holdbetter.shared.core_database.database

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import dev.holdbetter.shared.core_database.api.DatabaseApi
import dev.holdbetter.shared.core_database.impl.database.LeagueUserDatabase
import dev.holdbetter.shared.core_database.impl.database.LeagueUserDatabase.Companion.DATABASE_NAME

object Database {
    fun getInstance(context: Context): DatabaseApi {
        return buildDatabase(context)
    }

    private fun buildDatabase(context: Context): LeagueUserDatabase {
        return Room.databaseBuilder(
            context,
            LeagueUserDatabase::class.java,
            DATABASE_NAME
        ).setDriver(AndroidSQLiteDriver())
            .build()
    }
}