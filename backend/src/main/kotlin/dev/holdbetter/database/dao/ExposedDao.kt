package dev.holdbetter.database.dao

import kotlinx.coroutines.CoroutineDispatcher

internal interface ExposedDao {
    val dispatcher: CoroutineDispatcher
}