package dev.holdbetter.database

import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend inline fun <T> Database.query(
    dispatcher: CoroutineDispatcher,
    crossinline block: suspend () -> T
): T = newSuspendedTransaction(dispatcher, db = this) { block() }