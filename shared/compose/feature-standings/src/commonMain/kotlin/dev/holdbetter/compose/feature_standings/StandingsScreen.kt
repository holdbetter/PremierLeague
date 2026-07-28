package dev.holdbetter.compose.feature_standings

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import dev.holdbetter.common.GameResult
import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.compose.design_system.*
import dev.holdbetter.compose.design_system.components.PlatformStatusBar
import dev.holdbetter.compose.feature_standings.components.TeamRank
import dev.holdbetter.feature_standings_api.StandingsStore.State.Data.Standings.TeamRank
import dev.holdbetter.feature_standings_api.StandingsView
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import premierleague.shared.compose.feature_standings.generated.resources.*
import premierleague.shared.compose.feature_standings.generated.resources.Res
import kotlin.math.min

private val HeaderHeight = 56.dp

@Composable
internal fun StandingsScreen(
    model: StandingsView.Model,
    favoriteTeamIds: Set<Long>,
    dispatcher: suspend (StandingsView.Event) -> Unit
) {
    val scope = rememberCoroutineScope()

    val teams = model.standings?.teams
    val isLoading = model.isLoading

    val colors = LeagueTheme.colors

    PullToRefresh(
        isRefreshing = false,
        isEnabled = model.isRefreshEnabled,
        onRefresh = { scope.launch { dispatcher(StandingsView.Event.Reload) } },
    ) {
        Column {
            PlatformStatusBar(colors.primary)
            Box(Modifier.fillMaxSize()) {
                teams?.let { StandingsList(it, model, favoriteTeamIds, HeaderHeight) }
                HeaderText(HeaderHeight)
            }
        }
    }
    if (isLoading) LoaderLogo()
}

@Composable
private fun PullToRefresh(
    isRefreshing: Boolean,
    isEnabled: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        enabled = isEnabled,
        onRefresh = onRefresh,
        modifier = modifier,
        indicator = {
            Box(Modifier.padding(top = 14.dp).align(Alignment.TopCenter)) {
                Indicator(
                    state = state,
                    isRefreshing = isRefreshing,
                    containerColor = Raw.Colors.Purple400,
                    color = Raw.Colors.White,
                    modifier = Modifier.graphicsLayer {
                        scaleX = min(1 * state.distanceFraction, .85f)
                        scaleY = min(1 * state.distanceFraction, .85f)
                    },
                    maxDistance = PullToRefreshDefaults.IndicatorMaxDistance + 14.dp
                )
            }
        }
    ) {
        content()
    }
}

@Composable
private fun LoaderLogo() {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val size = 56.dp
        val offsetX = (maxWidth - size) * 0.51f
        val offsetY = (maxHeight - size) * 0.52f

        val infiniteTransition = rememberInfiniteTransition(label = "loader")
        val alphaF by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = .2f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 700,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "pulse_alpha",
        )

        Image(
            imageResource(DesignRes.drawable.league_logo_mini),
            colorFilter = ColorFilter.tint(Raw.Colors.Purple200),
            contentDescription = stringResource(DesignRes.string.premier_league_logo_content_desc),
            modifier = Modifier.size(size).offset(offsetX, offsetY).graphicsLayer {
                this.alpha = alphaF
            }
        )
    }
}

@Composable
private fun StandingsList(
    teams: List<TeamRank>,
    model: StandingsView.Model,
    favoriteTeamIds: Set<Long>,
    headerHeight: Dp
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .visible(!model.isLoading)
            .testTag("standings_list"),
        contentPadding = PaddingValues(top = headerHeight),
    ) {
        itemsIndexed(
            items = teams,
            key = { _: Int, team: TeamRank -> team.id },
        ) { index, team ->
            val isFavorite = team.id.toLong() in favoriteTeamIds
            val isNotLastItem = remember(teams.lastIndex, index) { index != teams.lastIndex }
            StandingsItem(team, isFavorite, isNotLastItem)
        }
    }
}

@Composable
private fun StandingsItem(
    team: TeamRank,
    isFavorite: Boolean,
    isNotLastItem: Boolean,
) {
    val textStyle = LeagueTheme.textStyle
    val colors = LeagueTheme.colors
    Row(
        modifier = Modifier.padding(start = 5.dp, top = 13.dp, bottom = 13.dp)
            .fillMaxWidth()
            .testTag("standings_row"),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 4.dp)
        ) {
            TeamRank(team)
            Spacer(Modifier.height(4.dp))
            TeamFavoriteIcon(isFavorite)
        }
        AsyncImage(
            model = team.image,
            modifier = Modifier.size(38.dp),
            contentDescription = stringResource(Res.string.team_logo_content_desc)
        )
        Column(
            modifier = Modifier.weight(1f)
                .padding(start = 13.dp, end = 8.dp, top = 2.dp)
        ) {
            TeamName(team)
            Row(Modifier.padding(start = 0.25.dp, top = 5.dp, bottom = 3.dp)) {
                team.lastResults.forEachIndexed { index, result ->
                    Box(
                        Modifier.background(
                            lastResultColors.themed(result),
                            RoundedCornerShape(2.dp)
                        ).width(15.dp)
                            .height(1.5.dp)
                    )
                    val notLast = index < team.lastResults.count() - 1
                    if (notLast) {
                        Spacer(Modifier.width(4.dp))
                    }
                }
            }
        }
        Column {
            Spacer(Modifier.height(5.dp))
            LiveIndicator(
                team.id,
                team.liveMatch,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(7.dp))
            SpBox(
                size = 18.sp,
                modifier = Modifier.background(Raw.Colors.Purple400, shape = CircleShape),
            ) {
                Text(
                    team.gamePlayed.toString(),
                    style = textStyle.headline3,
                    fontSize = 10.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
        Text(
            team.points.toString(),
            style = textStyle.headline2,
            textAlign = TextAlign.Center,
            fontSize = 26.sp,
            modifier = Modifier.padding(bottom = 2.dp, end = 12.dp)
                .defaultMinSize(45.dp)
        )
    }
    if (isNotLastItem) {
        Box(
            Modifier.fillMaxWidth()
                .height(1.dp)
                .background(colors.separator)
        )
    }
}

@Composable
private fun HeaderText(height: Dp) {
    val typography = LeagueTheme.typography
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier.height(height)
            .fillMaxWidth(.8f)
            .paint(
                painterResource(Res.drawable.standings_header_background),
                colorFilter = ColorFilter.tint(Raw.Colors.Purple),
                contentScale = ContentScale.FillBounds
            ).padding(horizontal = 45.dp)
    ) {
        Text(
            stringResource(Res.string.standings_header),
            color = Raw.Colors.White,
            fontFamily = typography.bold,
            fontSize = 26.dp.asTextUnit(),
        )
    }
}

@Composable
fun TeamFavoriteIcon(isFavorite: Boolean) {
    if (isFavorite) {
        Image(painterResource(Res.drawable.star_filled),
            contentDescription = stringResource(Res.string.team_favorite_icon),
            alignment = Alignment.TopCenter,
            colorFilter = ColorFilter.tint(Raw.Colors.Yellow),
            modifier = Modifier.size(11.dp)
        )
    }
}

@Composable
fun TeamName(team: TeamRank) {
    val style = LeagueTheme.textStyle.headline3

    Text(
        team.name,
        style = style,
        overflow = TextOverflow.MiddleEllipsis,
        fontSize = 17.5.sp,
        maxLines = 1
    )
}


@Composable
fun LiveIndicator(
    teamId: String,
    // TODO: Remove default value
    teamLiveMatch: MatchdayDTO? /*= MatchdayDTO(
        "0",
        "0",
        "2",
        "",
        "",
        "0",
        0,
        0,
        startDate = null,
        endDate = null,
    )*/,
    modifier: Modifier = Modifier
) {
    teamLiveMatch?.let {
        val isDark = isSystemInDarkTheme()
        val indicatorColor = remember(teamId, teamLiveMatch, isDark) {
            val isHomeMatch = teamId == it.teamHomeId
            val scoreDiff = it.resultHome.toInt() - it.resultAway.toInt()
            val diff = if (isHomeMatch) scoreDiff else -scoreDiff

            // TODO: design system?
            val indicatorColorName = when {
                diff == 0 -> "drawLiveColor"
                diff > 0 -> "winLiveColor"
                else -> "loseLiveColor"
            }

            mapOf(
                "drawLiveColor" to (Color(0xFFFFCE20) to Color(0xFFF4CC67)),
                "winLiveColor" to (Color(0xFF0EB848) to Color(0xFF1ED65D)),
                "loseLiveColor" to (Color(0xFFEA4E4E) to Color(0xFFEA4E4E)),
            )[indicatorColorName]!!.let { colorsOnTheme ->
                if (isDark) colorsOnTheme.second else colorsOnTheme.first
            }
        }
        val infiniteTransition = rememberInfiniteTransition(
            label = "pulse"
        )
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 900,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "pulse_scale",
        )

        Box(
            modifier = modifier.size(5.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .background(
                    color = indicatorColor,
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun SpBox(
    size: TextUnit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val density = LocalDensity.current
    val sizeDp = with(density) { size.toDp() }

    Box(
        modifier = modifier.size(sizeDp),
        contentAlignment = contentAlignment,
        content = content
    )
}

@Composable
fun Dp.asTextUnit(): TextUnit {
    val density = LocalDensity.current
    return with(density) {
        (this@asTextUnit.value / fontScale).sp
    }
}

val lastResultColors = mapOf(
    GameResult.WIN to (Color(0xFF2DB421) to Color(0xFF71C530)),
    GameResult.LOSE to (Color(0xFFD22222) to Color(0xFFBF2639)),
    GameResult.DRAW to (Color(0xFF6C6C6C) to Color(0xFF716759)),
)

@Composable
fun Map<GameResult, Pair<Color, Color>>.themed(result: GameResult): Color {
    val (day, night) = getValue(result)
    return if (isSystemInDarkTheme()) night else day
}