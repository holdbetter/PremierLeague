package dev.holdbetter.feature_standings_api

import kotlinx.coroutines.flow.Flow

interface StandingsRepository {
    suspend fun getStandings(): Flow<StandingsStore.State.Data.Standings>
}