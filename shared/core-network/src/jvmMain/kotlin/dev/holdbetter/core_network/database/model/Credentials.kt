package dev.holdbetter.core_network.database.model

import org.jetbrains.exposed.sql.Table

internal object Credentials : Table("CredentialsBasic") {
    val id = integer("id").autoIncrement()
    val key = text("key")
    val host = text("host")
    val service = text("service").nullable()
}