package dev.holdbetter.shared.feature_team_detail_impl.domain

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.shared.feature_team_detail.Match
import dev.holdbetter.shared.feature_team_detail.Team
import dev.holdbetter.shared.feature_team_detail.TeamDetailStore
import dev.holdbetter.shared.feature_team_detail.TeamDetailView
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object Mapper {
    fun dtoToState(teamRankDTO: TeamRankDTO): Team {
        return with(teamRankDTO) {
            Team(
                id = id,
                rank = rank,
                name = name,
                image = image,
                gamePlayed = gamePlayed,
                points = points,
                wins = wins,
                loses = loses,
                draws = draws,
                goalsFor = goalsFor,
                goalsAgainst = goalsAgainst,
                goalsDiff = goalsDiff,
                twitter = twitter
            )
        }
    }

    fun dtoToState(match: MatchdayDTO): Match {
        val userTimeZone = TimeZone.currentSystemDefault()
        return with(match) {
            Match(
                id = id,
                resultHome = resultHome,
                resultAway = resultAway,
                teamHomeId = teamHomeId,
                teamAwayId = teamAwayId,
                status = status,
                statusId = statusId,
                whoWon = whoWon,
                startDate = startDate?.toLocalDateTime(userTimeZone),
                endDate = endDate?.toLocalDateTime(userTimeZone),
                teamHome = teamHome,
                teamAway = teamAway
            )
        }
    }
}

fun TeamDetailView.Event.toIntent() =
    when (this) {
        TeamDetailView.Event.Reload -> TeamDetailStore.Intent.Reload
        TeamDetailView.Event.TwitterButtonClicked -> TeamDetailStore.Intent.RunTwitterRedirect
        TeamDetailView.Event.NavigationCommit -> TeamDetailStore.Intent.NavigationCommit
        TeamDetailView.Event.FavoritesClicked -> TeamDetailStore.Intent.ToggleFavorite
        is TeamDetailView.Event.DateClicked -> TeamDetailStore.Intent.MatchCardUpdate(date)
    }

fun TeamDetailStore.State.toModel() = TeamDetailView.Model(
    isLoading = isLoading,
    isRefreshEnabled = isRefreshEnabled,
    isError = data != null && data is Throwable,
    teamWithMatches = data as? TeamDetailStore.State.Data.TeamDetail,
    twitterRedirect = twitterRedirect
)