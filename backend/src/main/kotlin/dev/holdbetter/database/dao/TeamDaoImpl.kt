package dev.holdbetter.database.dao

import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.database.Mapper.toModel
import dev.holdbetter.database.Mapper.toTeamWithMatches
import dev.holdbetter.database.entity.Match
import dev.holdbetter.database.entity.Team
import dev.holdbetter.database.query
import dev.holdbetter.database.table.Standings
import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.BaseBatchInsertStatement

internal class TeamDaoImpl(
    override val dispatcher: CoroutineDispatcher,
    private val database: Database
) : TeamDao {

    override suspend fun getTeams(): List<TeamRankDTO> = database.query(dispatcher) {
        Team.all()
            .orderBy(Standings.points to SortOrder.DESC)
            .map(::toModel)
    }

    override suspend fun hasData() = database.query(dispatcher) {
        Standings.exists() && Standings.selectAll().empty().not()
    }

    override suspend fun getTeamWithMatches(teamId: String) =
        database.query(dispatcher) {
            Team.findById(teamId)?.load(
                Team::homeMatches,
                Team::awayMatches,
                Match::teamHome,
                Match::teamAway
            )?.toTeamWithMatches()
        }

    override suspend fun insertTeams(teams: List<TeamRankDTO>) {
        database.query(dispatcher) {
            Standings.batchInsert(
                data = teams,
                ignore = false,
                shouldReturnGeneratedValues = false
            ) { statementMapper(it) }
        }
    }

    override suspend fun updateTeams(teams: List<TeamRankDTO>) {
        database.query(dispatcher) {
            Standings.batchReplace(
                data = teams,
                shouldReturnGeneratedValues = false
            ) { statementMapper(it) }
        }
    }

    private fun BaseBatchInsertStatement.statementMapper(teamRank: TeamRankDTO) {
        this[Standings.id] = teamRank.id
        this[Standings.rank] = teamRank.rank
        this[Standings.name] = teamRank.name
        this[Standings.image] = teamRank.image
        this[Standings.gamePlayed] = teamRank.gamePlayed
        this[Standings.points] = teamRank.points
        this[Standings.wins] = teamRank.wins
        this[Standings.loses] = teamRank.loses
        this[Standings.draws] = teamRank.draws
        this[Standings.goalsFor] = teamRank.goalsFor
        this[Standings.goalsAgainst] = teamRank.goalsAgainst
        this[Standings.goalsDiff] = teamRank.goalsDiff
        this[Standings.alterImage] = null
    }
}