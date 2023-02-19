package dev.holdbetter.feature_standings_impl

import android.view.View
import androidx.core.view.isVisible
import dev.holdbetter.coreMvi.AbstractMviView
import dev.holdbetter.feature_standings_api.StandingsView
import dev.holdbetter.feature_standings_impl.databinding.StandingsFragmentBinding
import io.github.aakira.napier.Napier

internal class StandingsViewImpl(view: View) :
    AbstractMviView<StandingsView.Model, StandingsView.Event>(), StandingsView {

    private val binding = StandingsFragmentBinding.bind(view)
    private val adapter = StandingsAdapter()

    init {
        with(binding) {
            header.clipToOutline = true

            standingsList.adapter = adapter
            standingsList.addItemDecoration(StandingsDecorator(view.context))
        }
    }

    override fun render(model: StandingsView.Model) {
        // TODO: add debug flags
        Napier.d(message = model::toString)

        model.standings?.teams?.let(adapter::submitList)

        with(binding) {
            loader.isVisible = model.isLoading
            contentReadyGroup.isVisible = !model.isLoading
        }
    }
}