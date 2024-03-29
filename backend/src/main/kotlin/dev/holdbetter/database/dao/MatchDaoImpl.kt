package dev.holdbetter.database.dao

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.database.Mapper
import dev.holdbetter.database.entity.Match
import dev.holdbetter.database.query
import dev.holdbetter.database.table.Matches
import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.BaseBatchInsertStatement

internal class MatchDaoImpl(
    override val dispatcher: CoroutineDispatcher,
    private val database: Database
) : MatchDao {

    override suspend fun getSortedMatches(): List<MatchdayDTO> =
        database.query(dispatcher) {
            Match.all()
                .map(Mapper::toModel)
                .sortedBy(MatchdayDTO::startDate)
        }

    override suspend fun hasData(): Boolean =
        database.query(dispatcher) {
            Matches.exists() && Matches.selectAll().empty().not()
        }

    override suspend fun insertMatches(matches: List<MatchdayDTO>) {
        database.query(dispatcher) {
            Matches.batchInsert(
                data = matches,
                ignore = false,
                shouldReturnGeneratedValues = false
            ) { statementMapper(it) }
        }
    }

    override suspend fun updateMatches(matches: List<MatchdayDTO>) {
        database.query(dispatcher) {
            Matches.batchReplace(
                data = matches,
                shouldReturnGeneratedValues = false
            ) { statementMapper(it) }
        }
    }

    private fun BaseBatchInsertStatement.statementMapper(matchday: MatchdayDTO) {
        this[Matches.id] = matchday.id
        this[Matches.resultHome] = matchday.resultHome
        this[Matches.resultAway] = matchday.resultAway
        this[Matches.teamHome] = matchday.teamHomeId
        this[Matches.teamAway] = matchday.teamAwayId
        this[Matches.status] = matchday.status
        this[Matches.statusId] = matchday.statusId
        this[Matches.whoWon] = matchday.whoWon
        this[Matches.startDate] = matchday.startDate
        this[Matches.endDate] = matchday.endDate
    }
}