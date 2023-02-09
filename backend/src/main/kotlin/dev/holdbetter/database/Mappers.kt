package dev.holdbetter.database

import dev.holdbetter.model.Credentials
import dev.holdbetter.model.Credit
import org.jetbrains.exposed.sql.ResultRow

fun mapCredentialsToCredit(row: ResultRow) = Credit(
    apiKey = row[Credentials.key],
    host = row[Credentials.host],
    service = row[Credentials.service]
)