package dev.holdbetter.feature_standings_impl

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.holdbetter.assets.assetsColor
import dev.holdbetter.assets.assetsDrawable
import dev.holdbetter.assets.px
import dev.holdbetter.coreMvi.AbstractMviView
import dev.holdbetter.feature_standings_api.StandingsView
import dev.holdbetter.feature_standings_api.StandingsView.Event
import dev.holdbetter.feature_standings_api.StandingsView.Model
import dev.holdbetter.feature_standings_impl.databinding.StandingsFragmentBinding
import dev.holdbetter.shared.core_database.api.DatabaseApi
import dev.holdbetter.shared.core_navigation.Router
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class StandingsViewImpl(
    view: View,
    databaseApi: DatabaseApi,
    private val lifecycleScope: CoroutineScope,
    private val router: Router
) : AbstractMviView<Model, Event>(), StandingsView {

    private val favoriteDrawable =
        AppCompatResources.getDrawable(view.context, assetsDrawable.star_filled)
            ?.apply {
                setBounds(0, 0, 13.px.toInt(), 13.px.toInt())
            }

    private val binding = StandingsFragmentBinding.bind(view)
    private val adapter = StandingsAdapter(
        context = view.context,
        lifecycleScope = lifecycleScope,
        favoritesApi = databaseApi.favoritesApi(),
        favoriteDrawable = favoriteDrawable,
        onItemClickAction = ::teamOnStandingClick
    ).apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private val loaderAnimator = ValueAnimator.ofFloat(1f, 0.2f).apply {
        interpolator = AccelerateDecelerateInterpolator()
        duration = 700
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE

        addUpdateListener {
            binding.loader.alpha = it.animatedValue as Float
        }
    }

    init {
        with(binding) {
            setupPullToRefresh(binding.pullToRefresh, lifecycleScope)

            standingsList.adapter = adapter
            standingsList.addItemDecoration(StandingsDecorator(view.context))
        }
    }

    override fun render(model: Model) {
        // TODO: add debug flags
        Napier.d(message = model::toString)

        model.standings?.teams?.let(adapter::submitData)
        loading(model.isLoading)
        content(model)
        effect(model)
    }

    private fun loading(isLoading: Boolean) {
        with(binding) {
            loadingGroup.isVisible = isLoading

            if (isLoading) {
                loaderAnimator.start()
            } else {
                loaderAnimator.cancel()
            }
        }
    }

    private fun content(model: Model) {
        with(binding) {
            contentReadyGroup.isVisible = !model.isLoading

            pullToRefresh.isEnabled = model.isRefreshEnabled
            if (model.standings != null) pullToRefresh.isRefreshing = false
        }
    }

    private fun effect(model: Model) {
        model.selectedTeam?.let {
            lifecycleScope.launch { dispatch(Event.NavigationCommit) }
            router.navigateToTeam(it.id, it.imageUrl)
        }
    }

    private fun teamOnStandingClick(teamId: Long) {
        lifecycleScope.launch {
            dispatch(Event.TeamSelected(teamId.toString()))
        }
    }

    private fun setupPullToRefresh(
        pullToRefresh: SwipeRefreshLayout,
        lifecycleScope: CoroutineScope,
    ) {
        pullToRefresh.apply {
            val color = ContextCompat.getColor(context, assetsColor.purple_400)
            setProgressBackgroundColorSchemeColor(color)
            setColorSchemeResources(assetsColor.white)
            setSize(SwipeRefreshLayout.DEFAULT)

            doOnLayout {
                setProgressViewOffset(
                    true,
                    binding.headerText.height / 2,
                    (binding.standingsList.paddingTop * 1.5).toInt()
                )
            }

            isEnabled = false

            setOnRefreshListener {
                lifecycleScope.launch {
                    dispatch(Event.Reload)
                }
            }
        }
    }
}