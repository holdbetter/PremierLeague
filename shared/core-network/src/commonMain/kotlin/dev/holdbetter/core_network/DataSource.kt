package dev.holdbetter.core_network

import kotlinx.serialization.json.Json

sealed interface DataSource {
    interface Network : DataSource {
        val decoder: Json
        val networkInteractor: NetworkInteractor
    }

    interface Database : DataSource
    interface Local : DataSource
}