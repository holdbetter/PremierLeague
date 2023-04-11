package dev.holdbetter.shared.feature_team_detail_impl

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dev.holdbetter.assets.assetsColor
import dev.holdbetter.assets.isDarkMode
import dev.holdbetter.assets.updateColors
import dev.holdbetter.core_di_impl.findModuleDependency
import dev.holdbetter.shared.core_navigation.Destination
import dev.holdbetter.shared.core_navigation.di.NavigationModule
import dev.holdbetter.shared.feature_team_detail_impl.di.TeamDetailModule
import dev.holdbetter.shared.feature_team_detail_impl.di.TeamDetailRepositoryModule
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.properties.Delegates

class TeamDetailFragment : Fragment(R.layout.team_detail_fragment) {

    companion object {
        val tag = TeamDetailFragment::class.qualifiedName

        fun createFragment(teamId: Long, teamImage: String): TeamDetailFragment {
            return TeamDetailFragment().apply {
                arguments = bundleOf(
                    Destination.TeamDetail.Arguments.TEAM_ID to teamId,
                    Destination.TeamDetail.Arguments.TEAM_IMAGE to teamImage,
                )
            }
        }
    }

    private lateinit var module: TeamDetailModule
    private lateinit var component: TeamDetailComponent
    private lateinit var teamImage: String
    private var teamId by Delegates.notNull<Long>()

    private val leagueBackground by lazy {
        requireContext().getColor(assetsColor.leagueBackground)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        teamId = requireNotNull(
            arguments?.getString(Destination.TeamDetail.Arguments.TEAM_ID)?.toLong()
        )

        teamImage = requireNotNull(
            arguments?.getString(Destination.TeamDetail.Arguments.TEAM_IMAGE)
        ).run { URLDecoder.decode(this, StandardCharsets.UTF_8.toString()) }

        module = TeamDetailModule(
            teamId = teamId,
            navigationModule = NavigationModule(findNavController()),
            teamDetailRepositoryModule = TeamDetailRepositoryModule(
                networkModule = findModuleDependency(),
                databaseModule = findModuleDependency()
            )
        )

        component = TeamDetailComponent(
            module.store
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        bindViewColors(activity)

        component.onViewCreated(
            TeamDetailViewImpl(
                teamId = teamId,
                teamImageUrl = teamImage,
                lifecycleScope = lifecycleScope,
                view = view,
                isDarkMode = activity.isDarkMode(),
                router = module.router
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

    private fun bindViewColors(activity: Activity) {
        activity.window?.updateColors(
            status = leagueBackground,
            navigation = null,
            isLightText = activity.isDarkMode()
        )
    }
}