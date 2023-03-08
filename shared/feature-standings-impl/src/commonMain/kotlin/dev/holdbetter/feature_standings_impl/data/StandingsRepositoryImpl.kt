package dev.holdbetter.feature_standings_impl.data

import dev.holdbetter.feature_standings_api.StandingsRepository
import dev.holdbetter.feature_standings_api.StandingsStore
import dev.holdbetter.feature_standings_impl.domain.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class StandingsRepositoryImpl(
    private val dataSource: StandingsDataSource
) : StandingsRepository {

    override suspend fun getStandings(): Flow<StandingsStore.State.Data.Standings> {
        return flow {
            val standings = Mapper.toState(dataSource.getStandings())
            emit(standings)
        }
    }
}