package dev.holdbetter.feature_standings_impl

import android.view.View
import dev.holdbetter.coreMvi.AbstractMviView
import dev.holdbetter.feature_standings_api.StandingsView
import io.github.aakira.napier.Napier

internal class StandingsViewImpl(view: View) :
    AbstractMviView<StandingsView.Model, StandingsView.Event>(), StandingsView {

    init {

    }

    override fun render(model: StandingsView.Model) {
        Napier.d(message = model::toString)
    }
}