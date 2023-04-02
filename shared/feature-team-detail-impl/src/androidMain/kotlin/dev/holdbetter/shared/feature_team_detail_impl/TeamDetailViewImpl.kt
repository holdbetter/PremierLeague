package dev.holdbetter.shared.feature_team_detail_impl

import android.content.res.ColorStateList
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.net.Uri
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.ColorUtils
import androidx.core.view.doOnLayout
import androidx.palette.graphics.Palette
import dev.holdbetter.assets.*
import dev.holdbetter.common.Status
import dev.holdbetter.common.util.isRunning
import dev.holdbetter.coreMvi.AbstractMviView
import dev.holdbetter.shared.core_navigation.Router
import dev.holdbetter.shared.feature_team_detail.*
import dev.holdbetter.shared.feature_team_detail.TeamDetailView.Event
import dev.holdbetter.shared.feature_team_detail.TeamDetailView.Model
import dev.holdbetter.shared.feature_team_detail_impl.databinding.*
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

// TODO: Think about colors caching
internal class TeamDetailViewImpl(
    private val teamId: Long,
    private val lifecycleScope: CoroutineScope,
    view: View,
    private val isDarkMode: Boolean,
    private val router: Router
) : AbstractMviView<Model, Event>(), TeamDetailView {

    private val rootBinding = TeamDetailFragmentBinding.bind(view)
    private val statsBinding = StatsBlockBinding.bind(view)
    private val matchCardBinding = MatchCardBinding.bind(view)
    private val headerBinding = MatchHeaderBinding.bind(view)
    private val lastMatchesBinding = LastMatchesBinding.bind(view)

    private var palette: Palette? = null
    private var paletteCreator: Job? = null

    private val context
        get() = rootBinding.root.context

    private val matchesAdapter
        get() = lastMatchesBinding.matchGroups.adapter as MatchesAdapter

    private val statsNameProvider = StatsNameProvider(context)
    private val leagueBackground = context.getColor(assetsColor.leagueBackground)
    private val defaultTextColor = context.getColor(assetsColor.leagueTextColor)
    private val white = context.getColor(assetsColor.white)
    private val dateBackground =
        AppCompatResources.getDrawable(context, R.drawable.shape_card_date)?.mutate()
    private val timeBackground =
        AppCompatResources.getDrawable(context, R.drawable.shape_card_date)?.mutate()
    private val minuteBackground =
        AppCompatResources.getDrawable(context, R.drawable.shape_card_date)?.mutate()

    private val yearDecorator = YearDecorator(context, white)

    init {
        lastMatchesBinding.matchGroups.adapter = MatchesAdapter(
            lifecycleScope = lifecycleScope,
            teamId = teamId.toInt(),
            isDarkMode = isDarkMode,
            context = context
        )

        val spanCount = 3
        val recyclerMarginSideSum = context.px(R.dimen.cards_horizontal_margin) * 2
        val matchItemDimen = context.px(R.dimen.match_item)
        val verticalOffsetBtwRows =
            context.resources.getDimension(R.dimen.group_list_btw_row_margin)

        lastMatchesBinding.matchGroups.layoutManager = GroupLayoutManager(
            context,
            matchesAdapter,
            spanCount
        )

        lastMatchesBinding.matchGroups.itemAnimator = null

        view.doOnLayout {
            lastMatchesBinding.matchGroups.addItemDecoration(
                GroupGridDecorator(
                    context = context,
                    spanCount = spanCount,
                    verticalOffsetBtwRows = verticalOffsetBtwRows,
                    parentWidth = it.width,
                    itemWidth = matchItemDimen,
                    recyclerMarginHorizontal = recyclerMarginSideSum
                )
            )
        }

        matchCardBinding.matchCard.clipToOutline = true
        statsBinding.compareBtn.clipToOutline = true
        headerBinding.teamTwitter.clipToOutline = true
        headerBinding.teamFavorite.clipToOutline = true

        headerBinding.teamTwitter.setOnClickListener {
            onTwitterButtonClicked()
        }

        headerBinding.teamFavorite.setOnClickListener {
            lifecycleScope.launch {
                dispatch(Event.FavoritesClicked)
            }
        }

        rootBinding.calendar.adapter = DateAdapter(context, ::onCalendarDateClicked)
        rootBinding.calendar.addItemDecoration(MonthDecorator(context, defaultTextColor))
        rootBinding.calendar.addItemDecoration(yearDecorator)
    }

    override fun render(model: Model) {
        // TODO: add debug flags
        if (palette == null) {
            Napier.d(message = "no palette: $model")
        } else {
            Napier.d(message = "with palette: $model")
        }

        model.teamWithMatches?.let {
            val team = it.team
            val palette = palette

            if (palette != null) {
                val teamColor = palette.generateTeamColor(context)
                val actionDrawable = context.getActionDrawable(teamColor)

                effect(model)

                bindCard(it.matchCard, it.nextMatch, teamColor)
                bindCalendar(it.calendar, teamColor, defaultTextColor)
                bindHeader(team, it.isTeamFavorite, actionDrawable)
                bindStats(team, actionDrawable)
                bindMatches(teamColor, it.pastResultsByMonth)
            } else {
                if (paletteCreator?.isActive == true) {
                    paletteCreator?.cancel()
                }

                // create the palette first
                paletteCreator = lifecycleScope.launch {
                    val futureBitmap =
                        Uri.parse(team.image).getFutureBitmap(rootBinding.root.context)
                    this@TeamDetailViewImpl.palette = createPalette(futureBitmap)
                    render(model)
                }
            }
        }
    }

    private fun effect(model: Model) {
        if (model.twitterRedirect) {
            model.teamWithMatches?.team?.twitter?.let {
                lifecycleScope.launch { dispatch(Event.NavigationCommit) }
                router.handleThirdPartyLink(it)
            }
        }
    }

    private fun onTwitterButtonClicked() {
        lifecycleScope.launch {
            dispatch(Event.TwitterButtonClicked)
        }
    }

    private fun onCalendarDateClicked(date: LocalDate) {
        lifecycleScope.launch {
            dispatch(Event.DateClicked(date))
        }
    }

    private fun bindCalendar(
        calendar: List<DateHolder>,
        teamColor: Int,
        itemDefaultTextColor: Int
    ) {
        val adapter = rootBinding.calendar.adapter as? DateAdapter
        yearDecorator.setBackgroundColor(teamColor)
        adapter?.let {
            val oldListEmpty = adapter.currentList.isEmpty()

            adapter.submitData(
                calendar,
                accentColor(teamColor),
                itemDefaultTextColor
            )

            rootBinding.calendar.invalidateItemDecorations()

            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val nowPosition = adapter.currentList.indexOfFirst { it.date == now }
            val resultPosition = if (nowPosition != -1) {
                nowPosition + (adapter.itemCount / (DateAdapter.POSITION_MULTIPLIER / 2))
            } else {
                (adapter.itemCount / (DateAdapter.POSITION_MULTIPLIER / 2))
            }

            if (oldListEmpty) {
                rootBinding.calendar.scrollToPosition(resultPosition)
            }
        }
    }

    private fun bindHeader(
        team: Team,
        isTeamFavorite: Boolean,
        actionDrawable: Drawable
    ) {
        with(headerBinding) {
            teamTwitter.background = actionDrawable

            teamStar.setImageDrawable(getFavoriteImage(isTeamFavorite))
            teamFavorite.background = actionDrawable

            teamLogo.load(team.image)
            teamTitle.text = team.name
            teamRank.text = team.rank.toString()
        }
    }

    private fun bindCard(userMatch: Match, nextMatch: Match, @ColorInt teamColor: Int) {
        lifecycleScope.launch {
            val isHomeMatch = teamId == userMatch.teamHomeId.toLong()
            val oppositeImageUriString = userMatch.oppositeTeamImageUri(isHomeMatch)
            val oppositeFutureBitmap = Uri.parse(oppositeImageUriString).getFutureBitmap(context)
            val oppositePalette = createPalette(oppositeFutureBitmap)
            val oppositeTeamColor = oppositePalette.generateTeamColor(context)

            bindMatchCardStyle(teamColor)
            bindMatchScore(userMatch, isHomeMatch)
            bindMatchCardTitleText(userMatch, nextMatch)
            bindMatchCardHomeAwayData(userMatch, isHomeMatch, teamColor, oppositeTeamColor)
            bindMatchCardDate(userMatch, teamColor)
        }
    }

    private fun bindMatchScore(userMatch: Match, isHomeMatch: Boolean) {
        val scoreVisibility = when (userMatch.statusId) {
            Status.FULL_TIME.id -> View.VISIBLE
            Status.NOT_STARTED.id, Status.POSTPONED.id -> View.GONE
            else -> View.VISIBLE
        }

        matchCardBinding.score.visibility = scoreVisibility

        if (scoreVisibility == View.VISIBLE) {
            matchCardBinding.homeScore.text = userMatch.resultHome
            matchCardBinding.awayScore.text = userMatch.resultAway

            val homeScore = userMatch.resultHome.toInt()
            val awayScore = userMatch.resultAway.toInt()
            val diff = homeScore - awayScore
            matchCardBinding.score.backgroundTintList = if (isHomeMatch) {
                ColorStateList.valueOf(getScoreColor(diff))
            } else {
                ColorStateList.valueOf(getScoreColor(-diff))
            }
        }
    }

    private fun bindMatchCardDate(
        match: Match,
        teamColor: Int
    ) {
        val isRunning = match.statusId.isRunning

        val day = match.startDate?.dayOfMonth
        val month = match.startDate?.monthNumber
        val localDateTime = match.startDate?.toJavaLocalDateTime()
        val startTime = localDateTime?.toLocalTime()?.format(
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.ENGLISH)
        )?.lowercase()

        with(matchCardBinding) {
            val dateGradient = dateBackground as? GradientDrawable
            val timeGradient = timeBackground as? GradientDrawable
            val minuteGradient = minuteBackground as? GradientDrawable

            val (startColor, endColor) = getDateBackgroundColors(teamColor)

            dateGradient?.colors = intArrayOf(startColor, endColor)
            timeGradient?.colors = intArrayOf(startColor, endColor)

            dateStart.background = dateGradient
            timeStart.background = timeGradient

            timeStart.text = startTime
            dateStart.text = buildString {
                append(day)
                append("/")
                if (month != null && month < 10) append(0)
                append(month)
            }


            if (isRunning) {
                minuteGradient?.colors = intArrayOf(startColor, endColor)
                minute.background = minuteGradient
                minute.text = match.status
            }

            minute.visibility = if (isRunning) View.VISIBLE else View.GONE
        }
    }

    private fun getDateBackgroundColors(@ColorInt teamColor: Int): Pair<Int, Int> {
        val hsl = floatArrayOf(0f, 0f, 0f)
        ColorUtils.colorToHSL(teamColor, hsl)
        val (hue, sat, lum) = hsl
        hsl[2] = lum * 1.12f

        val startColor = ColorUtils.HSLToColor(hsl)

        hsl[1] = sat * .7f
        hsl[2] = lum * .8f
        val endColor = ColorUtils.HSLToColor(hsl)

        return startColor to endColor
    }

    private fun bindMatchCardHomeAwayData(
        match: Match,
        isHomeMatch: Boolean,
        teamColor: Int,
        oppositeTeamColor: Int
    ) {
        with(matchCardBinding) {

            homeTeamTitle.text = match.teamHome?.name
            awayTeamTitle.text = match.teamAway?.name

            val currentTeamColorList = ColorStateList.valueOf(teamColor)
            val oppositeTeamColorList = ColorStateList.valueOf(oppositeTeamColor)
            if (isHomeMatch) {
                homeTeamTitle.foregroundTintList = currentTeamColorList
                homeCorner.imageTintList = currentTeamColorList

                awayTeamTitle.foregroundTintList = oppositeTeamColorList
                awayCorner.imageTintList = oppositeTeamColorList
            } else {
                homeTeamTitle.foregroundTintList = oppositeTeamColorList
                homeCorner.imageTintList = oppositeTeamColorList

                awayTeamTitle.foregroundTintList = currentTeamColorList
                awayCorner.imageTintList = currentTeamColorList
            }
        }
    }

    private fun bindMatchCardTitleText(userMatch: Match, nextMatch: Match) {
        val futureMatchText = if (userMatch.startDate?.date == nextMatch.startDate?.date) {
            context.getString(R.string.next_match)
        } else {
            context.getString(R.string.upcoming_match)
        }

        matchCardBinding.cardTitle.text = when (userMatch.statusId) {
            Status.FULL_TIME.id -> context.getString(R.string.past_match)
            Status.NOT_STARTED.id, Status.POSTPONED.id -> futureMatchText
            else -> context.getString(R.string.on_going_match)
        }
    }

    private fun bindMatchCardStyle(teamColor: Int) {
        with(matchCardBinding) {
            matchCard.setTileColor(tileColor(teamColor, isDarkMode))
            matchCard.background = getCardGradient(teamColor)
        }
    }

    private fun bindMatches(@ColorInt teamColor: Int, matches: Map<Month, MonthResult>) {
        matchesAdapter.submitData(teamColor, matches)
    }

    private fun bindStats(team: Team, actionMiniDrawable: Drawable) {
        with(statsBinding) {
            compareBtn.background = actionMiniDrawable

            games.statsName.text = statsNameProvider.gamesName
            games.statsValue.text = team.gamePlayed.toString()

            won.statsName.text = statsNameProvider.wonName
            won.statsValue.text = team.wins.toString()

            draws.statsName.text = statsNameProvider.drawsName
            draws.statsValue.text = team.draws.toString()

            loses.statsName.text = statsNameProvider.losesName
            loses.statsValue.text = team.loses.toString()

            goalsFor.statsName.text = statsNameProvider.goalsForName
            goalsFor.statsValue.text = team.goalsFor.toString()

            goalsAgainst.statsName.text = statsNameProvider.goalsAgainstName
            goalsAgainst.statsValue.text = team.goalsAgainst.toString()
        }
    }

    private fun getCardGradient(teamColor: Int): PaintDrawable {
        val gradientStartColor = cardStartColor(teamColor, leagueBackground, isDarkMode)
        val gradientEndColor = cardEndColor(teamColor, leagueBackground, isDarkMode)

        val shader = object : ShapeDrawable.ShaderFactory() {
            override fun resize(width: Int, height: Int): Shader {
                val x0 = width * .3f
                val x1 = width * .5f
                return LinearGradient(
                    x0,
                    0f,
                    x1,
                    height.toFloat(),
                    intArrayOf(gradientStartColor, gradientEndColor),
                    floatArrayOf(0f, 1f),
                    Shader.TileMode.CLAMP
                )
            }
        }

        val cardRadius = context.resources.getDimension(R.dimen.match_card_radius)
        return PaintDrawable().apply {
            shape = RectShape()
            shaderFactory = shader
            setCornerRadius(cardRadius)
        }
    }

    private fun Match.oppositeTeamImageUri(isHomeMatch: Boolean) = if (isHomeMatch) {
        teamAway?.image
    } else {
        teamHome?.image
    }

    private fun getFavoriteImage(isTeamFavorite: Boolean): Drawable? {
        return if (isTeamFavorite) {
            AppCompatResources.getDrawable(context, assetsDrawable.star_filled)
        } else {
            AppCompatResources.getDrawable(context, assetsDrawable.star_outlined)
        }
    }

    @ColorInt
    private fun getScoreColor(scoreDiff: Int): Int {
        return when {
            scoreDiff == 0 -> context.getColor(R.color.match_card_score_draw)
            scoreDiff < -2 -> context.getColor(R.color.match_card_score_big_lose)
            scoreDiff > 2 -> context.getColor(R.color.match_card_score_big_win)
            scoreDiff < 0 -> context.getColor(R.color.match_card_score_little_lose)
            else -> context.getColor(R.color.match_card_score_little_win)
        }
    }
}