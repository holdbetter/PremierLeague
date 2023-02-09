package dev.holdbetter.feature_standings_impl

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class StandingsFragment : Fragment(R.layout.standings_fragment) {

    private lateinit var component: StandingsComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = StandingsComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.onViewCreated(StandingsViewImpl(view))
    }

    override fun onStart() {
        super.onStart()
        component.onStart()
    }

    override fun onStop() {
        component.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        component.onViewDestroyed()
        super.onDestroyView()
    }

    override fun onDestroy() {
        component.onDestroy()
        super.onDestroy()
    }
}