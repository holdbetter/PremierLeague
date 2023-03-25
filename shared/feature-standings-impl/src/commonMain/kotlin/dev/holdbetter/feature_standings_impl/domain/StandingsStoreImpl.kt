package dev.holdbetter.feature_standings_impl.domain

import dev.holdbetter.coreMvi.StoreHelper
import dev.holdbetter.feature_standings_api.StandingsRepository
import dev.holdbetter.feature_standings_api.StandingsStore
import dev.holdbetter.feature_standings_api.StandingsStore.Intent
import dev.holdbetter.feature_standings_api.StandingsStore.State
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
internal class StandingsStoreImpl(
    private val repository: StandingsRepository
) : AbstractFlow<State>(), StandingsStore {

    private sealed interface Effect {
        object LoadingStarted : Effect
        class LoadingFinished(val standings: State.Data.Standings) : Effect
        class LoadingError(val throwable: Throwable) : Effect
        class NavigationStarted(val teamRank: State.Data.Standings.TeamRank) : Effect
        object NavigationCommited : Effect
    }

    private val dispatcher = Dispatchers.Default
    private val scope = CoroutineScope(dispatcher + SupervisorJob())

    private val helper = StoreHelper(
        initialState = State(),
        actor = ::handleIntent,
        reducer = ::reduce,
        scope = scope
    )

    override val state: State
        get() = helper.state

    override suspend fun collectSafely(collector: FlowCollector<State>) =
        helper.collectSafely(collector)

    override suspend fun accept(intent: Intent) = helper.accept(intent)

    override fun dispose() = helper.dispose()

    init {
        scope.launch {
            accept(Intent.Reload)
        }
    }

    private suspend fun handleIntent(state: State, intent: Intent): Flow<Effect> =
        when (intent) {
            is Intent.OpenTeamDetail -> openTeamDetail(state, intent.teamId)
            Intent.Reload -> withLoading(::reload)
        }

    private fun reduce(state: State, effect: Effect) =
        when (effect) {
            Effect.LoadingStarted -> state.copy(
                isLoading = true,
                isRefreshEnabled = false
            )
            is Effect.LoadingError -> state.copy(
                isLoading = false,
                isRefreshEnabled = true,
                data = State.Data.Error(effect.throwable)
            )
            is Effect.LoadingFinished -> state.copy(
                isLoading = false,
                isRefreshEnabled = true,
                data = effect.standings
            )
            is Effect.NavigationStarted -> state.copy(
                isLoading = false,
                selectedTeam = effect.teamRank
            )
            Effect.NavigationCommited -> state.copy(
                isLoading = false,
                selectedTeam = null
            )
        }

    private suspend fun reload(): Flow<Effect> =
        repository.getStandings()
            .map<State.Data.Standings, Effect>(Effect::LoadingFinished)
            .onCompletion { reason -> reason?.let(Effect::LoadingError) }

    private fun openTeamDetail(state: State, teamId: String): Flow<Effect> =
        flow {
            state.data
                ?.getTeamById(teamId)
                ?.also { emit(Effect.NavigationStarted(it)) }

            // TODO: Replace with better solution
            emit(Effect.NavigationCommited)
        }

    private fun State.Data.getTeamById(teamId: String): State.Data.Standings.TeamRank? {
        return if (this is State.Data.Standings) {
            this.teams.firstOrNull { teamRank -> teamRank.id == teamId }
        } else {
            null
        }
    }

    private fun withLoading(block: suspend () -> Flow<Effect>): Flow<Effect> = flow {
        emit(Effect.LoadingStarted)
        delay(300)
        emitAll(block())
    }
}