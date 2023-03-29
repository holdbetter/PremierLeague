package dev.holdbetter.database.dao

import dev.holdbetter.common.MatchdayDTO

internal interface MatchDao : ExposedDao {

    suspend fun getSortedMatches(): List<MatchdayDTO>

    suspend fun insertMatches(matches: List<MatchdayDTO>)
    suspend fun updateMatches(matches: List<MatchdayDTO>)

    suspend fun hasData(): Boolean
}