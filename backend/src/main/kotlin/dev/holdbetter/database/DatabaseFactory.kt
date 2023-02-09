package dev.holdbetter.database

import dev.holdbetter.util.Mode
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {

    private const val PG_USER = "PGUSER"
    private const val PG_PASSWORD = "PGPASSWORD"
    private const val PG_HOST = "PGHOST"
    private const val PG_PORT = "PGPORT"
    private const val PG_DATABASE = "PGDATABASE"
    private const val DRIVER_CLASS_NAME = "org.postgresql.Driver"

    private val pgUser = System.getenv(PG_USER)
    private val pgPassword = System.getenv(PG_PASSWORD)
    private val pgHost = System.getenv(PG_HOST)
    private val pgPort = System.getenv(PG_PORT)
    private val pgDatabase = System.getenv(PG_DATABASE)

    fun init(mode: Mode) = if (mode.isDevelopment) {
        // local database connection
        Database.connect(getPostgresSqlUrl(), DRIVER_CLASS_NAME)
    } else {
        // remote db connection
        Database.connect(
            getPostgresSqlUrl(
                pgUser = pgUser,
                pgPassword = pgPassword,
                pgHost = pgHost,
                pgPort = pgPort,
                pgDatabase
            )
        )
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun getPostgresSqlUrl(
        pgUser: String = "vievseev",
        pgPassword: String = "",
        pgHost: String = "localhost",
        pgPort: String = "5432",
        pgDatabase: String = "vievseev"
    ) = "jdbc:postgresql://$pgHost:$pgPort/$pgDatabase?user=$pgUser&password=$pgPassword"
}