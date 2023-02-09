package dev.holdbetter.database.dao

import dev.holdbetter.database.mapCredentialsToCredit
import dev.holdbetter.model.Credentials
import dev.holdbetter.routes.ApiFootballConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class CreditDaoImpl(private val database: Database) : CreditDao {
    override fun getCredit() = transaction(database) {
        Credentials.selectAll()
            .map(::mapCredentialsToCredit)
            .first { it.service == ApiFootballConfig.SERVICE_NAME }
    }
}