package dev.holdbetter.shared.core_database.di

import dev.holdbetter.core_di_api.folder.HasSharedDependencies
import dev.holdbetter.shared.core_database.api.DatabaseApi

class DatabaseModule(
    val databaseApi: DatabaseApi
) : HasSharedDependencies