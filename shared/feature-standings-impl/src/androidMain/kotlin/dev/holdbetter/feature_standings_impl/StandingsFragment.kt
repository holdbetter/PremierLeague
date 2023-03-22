package dev.holdbetter.feature_standings_impl

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dev.holdbetter.assets.assetsColor
import dev.holdbetter.assets.isDarkMode
import dev.holdbetter.assets.updateColors
import dev.holdbetter.core_di_impl.findModuleDependency
import dev.holdbetter.feature_standings_impl.di.StandingsModule
import dev.holdbetter.feature_standings_impl.di.StandingsRepositoryModule

class StandingsFragment : Fragment(R.layout.standings_fragment) {

    private lateinit var component: StandingsComponent
    private lateinit var module: StandingsModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        module = StandingsModule(
            StandingsRepositoryModule(
                networkModule = findModuleDependency()
            )
        )

        val purple = requireActivity().getColor(assetsColor.leagueColorPrimary)
        requireActivity().window.updateColors(
            status = purple,
            navigation = purple,
            isDarkMode = requireContext().isDarkMode()
        )

        component = StandingsComponent(module.store)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.onViewCreated(StandingsViewImpl(lifecycleScope, view))
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