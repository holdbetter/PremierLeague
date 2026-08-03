package dev.holdbetter.shared.core_database.database

import androidx.room3.Room
import androidx.sqlite.driver.NativeSQLiteDriver
import dev.holdbetter.shared.core_database.api.DatabaseApi
import dev.holdbetter.shared.core_database.impl.database.LeagueUserDatabase
import dev.holdbetter.shared.core_database.impl.database.LeagueUserDatabase.Companion.DATABASE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

object Database {
    fun getInstance(): DatabaseApi {
        return buildDatabase()
    }

    fun buildDatabase(): LeagueUserDatabase {
        val dbFilePath = documentDirectory() + "/${DATABASE_NAME}.db"
        return Room.databaseBuilder<LeagueUserDatabase>(
            name = dbFilePath,
        ).setDriver(NativeSQLiteDriver())
            .build()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }
}