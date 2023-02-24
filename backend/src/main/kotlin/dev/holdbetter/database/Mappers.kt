package dev.holdbetter.database

import dev.holdbetter.database.model.Credentials
import dev.holdbetter.innerApi.model.Credit
import org.jetbrains.exposed.sql.ResultRow

fun mapCredentialsToCredit(row: ResultRow) = Credit(
    apiKey = row[Credentials.key],
    host = row[Credentials.host],
    service = row[Credentials.service]
)