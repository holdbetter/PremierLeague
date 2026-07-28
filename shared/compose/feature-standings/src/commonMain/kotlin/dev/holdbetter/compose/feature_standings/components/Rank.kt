package dev.holdbetter.compose.feature_standings.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.holdbetter.compose.design_system.LeagueTheme
import dev.holdbetter.feature_standings_api.StandingsStore.State

@Composable
internal fun TeamRank(team: State.Data.Standings.TeamRank) {
    val colors = LeagueTheme.colors

    Text(
        text = team.rank.toString(),
        color = colors.textColorSecondary,
        fontFamily = LeagueTheme.typography.medium,
        fontSize = 15.sp,
        modifier = Modifier.width(40.dp),
        textAlign = TextAlign.Center,
    )
}
