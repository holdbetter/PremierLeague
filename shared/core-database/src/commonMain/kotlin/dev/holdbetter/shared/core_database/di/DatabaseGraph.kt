package dev.holdbetter.shared.core_database.di

import dev.holdbetter.shared.core_database.api.DatabaseApi

interface DatabaseGraph {
    val databaseApi: DatabaseApi
}