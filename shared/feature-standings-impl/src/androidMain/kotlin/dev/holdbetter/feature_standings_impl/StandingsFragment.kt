package dev.holdbetter.feature_standings_impl

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dev.holdbetter.assets.assetsColor
import dev.holdbetter.assets.updateColors
import dev.holdbetter.core_di_impl.findModuleDependency
import dev.holdbetter.feature_standings_impl.di.StandingsModule
import dev.holdbetter.feature_standings_impl.di.StandingsRepositoryModule
import dev.holdbetter.shared.core_navigation.di.NavigationModule

class StandingsFragment : Fragment(R.layout.standings_fragment) {

    private lateinit var component: StandingsComponent
    private lateinit var module: StandingsModule

    private val primaryColor by lazy {
        requireActivity().getColor(assetsColor.leagueColorPrimary)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        module = StandingsModule(
            navigationModule = NavigationModule(findNavController()),
            databaseModule = findModuleDependency(),
            standingsRepositoryModule = StandingsRepositoryModule(
                networkModule = findModuleDependency()
            )
        )

        component = StandingsComponent(module.store)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.updateColors(
            status = primaryColor,
            navigation = primaryColor,
            isLightText = true
        )

        component.onViewCreated(
            StandingsViewImpl(
                lifecycleScope = lifecycleScope,
                view = view,
                router = module.router,
                databaseApi = module.database
            )
        )
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