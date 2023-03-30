package dev.holdbetter.shared.feature_team_detail_impl.domain

import dev.holdbetter.coreMvi.StoreHelper
import dev.holdbetter.shared.feature_team_detail.TeamDetailRepository
import dev.holdbetter.shared.feature_team_detail.TeamDetailStore
import dev.holdbetter.shared.feature_team_detail.TeamDetailStore.State
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
internal class TeamDetailStoreImpl(
    teamId: Long,
    private val repository: TeamDetailRepository
) : AbstractFlow<State>(), TeamDetailStore {

    private sealed interface Effect {
        object LoadingStarted : Effect
        class LoadingFinished(val teamDetail: State.Data.TeamDetail) : Effect
        class LoadingError(val throwable: Throwable) : Effect
        object TwitterNavigation : Effect
        object AddedToFavorites : Effect
        object RemovedFromFavorites : Effect
        object NavigationCleanState : Effect
    }

    private val dispatcher = Dispatchers.Default
    private val scope = CoroutineScope(dispatcher + SupervisorJob())

    private val helper: StoreHelper<TeamDetailStore.Intent, Effect, State> = StoreHelper(
        initialState = State(teamId),
        actor = ::handleIntent,
        reducer = ::reducer,
        scope = scope
    )

    init {
        scope.launch {
            accept(TeamDetailStore.Intent.Reload)
        }
    }

    override val state: State
        get() = helper.state

    override suspend fun collectSafely(collector: FlowCollector<State>) =
        helper.collectSafely(collector)

    override fun dispose() = helper.dispose()

    override suspend fun accept(intent: TeamDetailStore.Intent) = helper.accept(intent)

    private suspend fun handleIntent(state: State, intent: TeamDetailStore.Intent): Flow<Effect> {
        return when (intent) {
            is TeamDetailStore.Intent.Reload -> withLoading {
                reload(state.teamId)
            }
            is TeamDetailStore.Intent.RunTwitterRedirect -> flowOf(Effect.TwitterNavigation)
            TeamDetailStore.Intent.NavigationCommit -> flowOf(Effect.NavigationCleanState)
            TeamDetailStore.Intent.ToggleFavorite -> toggleFavorites(state.teamId)
        }
    }

    private fun reducer(state: State, effect: Effect): State {
        return when (effect) {
            is Effect.LoadingError -> state.copy(
                isLoading = false,
                isRefreshEnabled = true,
                data = State.Data.Error(effect.throwable)
            )
            is Effect.LoadingFinished -> state.copy(
                isLoading = false,
                isRefreshEnabled = true,
                data = effect.teamDetail
            )
            Effect.LoadingStarted -> state.copy(
                isLoading = true,
                isRefreshEnabled = false
            )
            is Effect.TwitterNavigation -> state.copy(
                twitterRedirect = true
            )
            Effect.NavigationCleanState -> state.copy(
                twitterRedirect = false
            )
            Effect.AddedToFavorites -> state.copy(
                data = (state.data as? State.Data.TeamDetail)?.copy(
                    isTeamFavorite = true
                )
            )
            Effect.RemovedFromFavorites -> state.copy(
                data = (state.data as? State.Data.TeamDetail)?.copy(
                    isTeamFavorite = false
                )
            )
        }
    }

    private suspend fun reload(teamId: Long): Flow<Effect> {
        return flowOf(repository.getTeamDetail(teamId))
            .map { Effect.LoadingFinished(it) }
            .onCompletion { reason -> reason?.let(Effect::LoadingError) }
    }

    private suspend fun toggleFavorites(teamId: Long): Flow<Effect> {
        return flowOf(repository.changeTeamFavorite(teamId))
            .map {
                if (it) {
                    Effect.AddedToFavorites
                } else {
                    Effect.RemovedFromFavorites
                }
            }
    }

    private fun withLoading(block: suspend () -> Flow<Effect>): Flow<Effect> = flow {
        emit(Effect.LoadingStarted)
        emitAll(block())
    }
}