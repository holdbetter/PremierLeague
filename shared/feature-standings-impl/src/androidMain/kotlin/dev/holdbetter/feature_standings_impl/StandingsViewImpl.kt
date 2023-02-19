package dev.holdbetter.feature_standings_impl

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.isVisible
import dev.holdbetter.coreMvi.AbstractMviView
import dev.holdbetter.feature_standings_api.StandingsView
import dev.holdbetter.feature_standings_impl.databinding.StandingsFragmentBinding
import io.github.aakira.napier.Napier

internal class StandingsViewImpl(view: View) :
    AbstractMviView<StandingsView.Model, StandingsView.Event>(), StandingsView {

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
            standingsList.adapter = adapter
            standingsList.addItemDecoration(StandingsDecorator(view.context))
        }
    }

    override fun render(model: StandingsView.Model) {
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

    private fun content(model: StandingsView.Model) {
        binding.contentReadyGroup.isVisible = !model.isLoading
    }
}