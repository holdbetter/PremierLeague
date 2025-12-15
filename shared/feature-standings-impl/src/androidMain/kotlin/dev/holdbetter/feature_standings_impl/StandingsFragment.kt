package dev.holdbetter.feature_standings_impl

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dev.holdbetter.assets.InsetsHandler
import dev.holdbetter.assets.PremierFragment
import dev.holdbetter.assets.assetsColor
import dev.holdbetter.assets.updateWindowView
import dev.holdbetter.core_di_impl.findModuleDependency
import dev.holdbetter.feature_standings_impl.di.StandingsModule
import dev.holdbetter.feature_standings_impl.di.StandingsRepositoryModule
import dev.holdbetter.shared.core_navigation.di.NavigationModule

class StandingsFragment : PremierFragment(R.layout.standings_fragment) {

    private lateinit var component: StandingsComponent
    private lateinit var module: StandingsModule

    private val primaryColor by lazy {
        requireActivity().getColor(assetsColor.leagueColorPrimary)
    }

    override val insetsHandler = InsetsHandler {  view, _ ->
        val statusBar = view.findViewById<View>(R.id.status_bar)
        val standingsList = view.findViewById<View>(R.id.standings_list)

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars())

            statusBar.updateLayoutParams<ConstraintLayout.LayoutParams> {
                height = statusBarHeight.top
            }

            standingsList.updatePadding(top =
                resources.getDimensionPixelSize(R.dimen.results_header_height) +
                        statusBarHeight.top
            )

            insets
        }
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

        windowState.updateWindowView(
            hideNavigationBar = true,
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