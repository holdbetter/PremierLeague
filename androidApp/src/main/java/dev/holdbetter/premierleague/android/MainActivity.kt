package dev.holdbetter.premierleague.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import dev.holdbetter.feature_standings_impl.StandingsFragment
import dev.holdbetter.shared.core_navigation.Destination
import dev.holdbetter.shared.core_navigation.createGraph
import dev.holdbetter.shared.feature_team_detail_impl.TeamDetailFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlin.reflect.KClass

internal class MainActivity : AppCompatActivity() {

    // Can be moved with other big features modules on demand
    private val moduleFragments: Map<KClass<out Fragment>, Destination> = mapOf(
        StandingsFragment::class to Destination.Standings,
        TeamDetailFragment::class to Destination.TeamDetail
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()

        super.onCreate(savedInstanceState)

        val splashAnimationDelay = lifecycleScope.async(Dispatchers.IO) {
            delay(800)
        }

        splash.setKeepOnScreenCondition { !splashAnimationDelay.isCompleted }

        setContentView(R.layout.activity_main)
        createNavigationGraph()
    }

    private fun createNavigationGraph() {
        (supportFragmentManager.findFragmentById(R.id.root) as? NavHostFragment)
            ?.navController
            ?.apply {
                graph = createGraph(Destination.Standings) {
                    moduleFragments.forEach {
                        registerFragment(it.value, it.key)
                    }
                }
            }
    }

    private fun NavGraphBuilder.registerFragment(
        destination: Destination,
        fragmentClass: KClass<out Fragment>
    ) {
        destination(
            FragmentNavigatorDestinationBuilder(
                provider[FragmentNavigator::class],
                destination.destination,
                fragmentClass
            ).apply { config(destination) }
        )
    }

    private fun FragmentNavigatorDestinationBuilder.config(destination: Destination) {
        for (argumentName in destination.argumentNames) {
            argument(argumentName) {
                type = NavType.StringType
            }
        }
    }
}