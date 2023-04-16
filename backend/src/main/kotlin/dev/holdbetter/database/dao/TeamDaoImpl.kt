package dev.holdbetter.database.dao

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.common.util.isGameOver
import dev.holdbetter.common.util.isRunning
import dev.holdbetter.database.Mapper.constructTeamMatches
import dev.holdbetter.database.Mapper.toStandings
import dev.holdbetter.database.Mapper.toTeamWithMatches
import dev.holdbetter.database.entity.Match
import dev.holdbetter.database.entity.Team
import dev.holdbetter.database.query
import dev.holdbetter.database.table.Standings
import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.BaseBatchInsertStatement

internal class TeamDaoImpl(
    override val dispatcher: CoroutineDispatcher,
    private val database: Database
) : TeamDao {

    override suspend fun getStandings(): List<TeamRankDTO> = database.query(dispatcher) {
        Team.all()
            .with(Team::homeMatches, Team::awayMatches)
            .orderBy(Standings.rank to SortOrder.ASC)
            .map {
                val matches = it.constructTeamMatches()
                toStandings(it, lastFiveMatches(matches), liveMatch(matches))
            }
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

    private fun lastFiveMatches(matches: List<MatchdayDTO>) =
        matches.filter { it.statusId.isGameOver }
            .sortedBy { it.startDate }
            .takeLast(5)

    private fun liveMatch(matches: List<MatchdayDTO>) = matches.firstOrNull { it.statusId.isRunning }

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
        this[Standings.alterImageId] = teamRank.alterImageId
        this[Standings.twitter] = teamRank.twitter
    }
}