package dev.holdbetter.core_network.database

import dev.holdbetter.core_network.database.model.Credentials
import dev.holdbetter.core_network.model.Credit
import org.jetbrains.exposed.sql.ResultRow

internal fun mapCredentialsToCredit(row: ResultRow) = Credit(
    apiKey = row[Credentials.key],
    host = row[Credentials.host],
    service = row[Credentials.service]
)