package dev.holdbetter.feature_standings_impl

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.holdbetter.assets.assetsColor
import dev.holdbetter.coreMvi.AbstractMviView
import dev.holdbetter.feature_standings_api.StandingsView
import dev.holdbetter.feature_standings_api.StandingsView.Event
import dev.holdbetter.feature_standings_api.StandingsView.Model
import dev.holdbetter.feature_standings_impl.databinding.StandingsFragmentBinding
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class StandingsViewImpl(
    lifecycleScope: CoroutineScope,
    view: View
) : AbstractMviView<Model, Event>(), StandingsView {

    private val binding = StandingsFragmentBinding.bind(view)
    private val adapter = StandingsAdapter()

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

        model.standings?.teams?.let(adapter::submitList)
        loading(model.isLoading)
        content(model)
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