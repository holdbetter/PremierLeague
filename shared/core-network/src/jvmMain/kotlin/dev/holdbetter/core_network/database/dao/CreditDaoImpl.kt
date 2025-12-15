package dev.holdbetter.core_network.database.dao

import dev.holdbetter.core_di_api.folder.Dikt
import dev.holdbetter.core_network.database.mapCredentialsToCredit
import dev.holdbetter.core_network.database.model.Credentials
import dev.holdbetter.core_network.model.RemoteLivescoreConfig
import dev.shustoff.dikt.InjectableSingleInScope
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

// TODO: Test
internal class CreditDaoImpl(
    private val database: Database
) : CreditDao, InjectableSingleInScope<Dikt> {
    override fun getCredit() = transaction(database) {
        Credentials.selectAll()
            .map(::mapCredentialsToCredit)
            .first { it.service == RemoteLivescoreConfig.SERVICE_NAME }
    }
}