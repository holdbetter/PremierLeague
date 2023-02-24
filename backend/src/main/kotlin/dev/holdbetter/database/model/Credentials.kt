package dev.holdbetter.database.model

import org.jetbrains.exposed.sql.Table

object Credentials : Table("CredentialsBasic") {
    val id = integer("id").autoIncrement()
    val key = text("key")
    val host = text("host")
    val service = text("service").nullable()
}