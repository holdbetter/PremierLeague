package dev.holdbetter.core_network.database.dao

import dev.holdbetter.core_network.database.mapCredentialsToCredit
import dev.holdbetter.core_network.database.model.Credentials
import dev.holdbetter.core_network.model.RemoteLivescoreConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

// TODO: Test
internal class CreditDaoImpl(private val database: Database) : CreditDao {
    override fun getCredit() = transaction(database) {
        Credentials.selectAll()
            .map(::mapCredentialsToCredit)
            .first { it.service == RemoteLivescoreConfig.SERVICE_NAME }
    }
}