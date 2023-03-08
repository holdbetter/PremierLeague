package dev.holdbetter.core_network

import kotlinx.serialization.json.Json

sealed interface DataSource {
    interface Network : DataSource {
        /**
         * Array of paths without separator.
         * Order sensitive
         *
         * Example:
         *
         * | Array                               | Path to request             |
         * | :-----------------------------      | :------------------------   |
         * | `"books", "order", "cart"    ->  `  | `"/books/order/cart"`       |
         *
         */
        val paths: Array<String>
        val decoder: Json
        val networkInteractor: NetworkInteractor
    }

    interface Database : DataSource
    interface Local : DataSource
}