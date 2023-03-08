package dev.holdbetter.database.dao

import dev.holdbetter.innerApi.model.Matchday

internal interface MatchDao : ExposedDao {

    suspend fun getMatches(): List<Matchday>

    suspend fun insertMatches(matches: List<Matchday>)
    suspend fun updateMatches(matches: List<Matchday>)

    suspend fun hasData(): Boolean
}