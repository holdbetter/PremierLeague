package dev.holdbetter.core_network.database

import dev.holdbetter.core_network.util.Mode
import org.jetbrains.exposed.sql.Database

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
                pgDatabase = pgDatabase
            )
        )
    }

    // TODO: Test
    private fun getPostgresSqlUrl(
        pgUser: String = "holdbetter",
        pgPassword: String = "",
        pgHost: String = "localhost",
        pgPort: String = "5432",
        pgDatabase: String = "holdbetter"
    ) = "jdbc:postgresql://$pgHost:$pgPort/$pgDatabase?user=$pgUser&password=$pgPassword"
}