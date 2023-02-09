package dev.holdbetter.model

import org.jetbrains.exposed.sql.Table

data class Credit(
    val apiKey: String,
    val host: String,
    val service: String?
)

object Credentials : Table("CredentialsBasic") {
    val id = integer("id").autoIncrement()
    val key = text("key")
    val host = text("host")
    val service = text("service").nullable()
}