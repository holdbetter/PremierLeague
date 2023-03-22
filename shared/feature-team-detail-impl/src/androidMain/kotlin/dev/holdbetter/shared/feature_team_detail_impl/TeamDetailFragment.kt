package dev.holdbetter.shared.feature_team_detail_impl

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dev.holdbetter.assets.isDarkMode
import dev.holdbetter.shared.feature_team_detail_impl.di.TeamDetailModule
import dev.holdbetter.shared.feature_team_detail_impl.di.TeamDetailRepositoryModule
import kotlin.properties.Delegates

class TeamDetailFragment : Fragment(R.layout.team_detail_fragment) {

    companion object {
        private const val TEAM_ID_ARG = "teamId"
        val tag = TeamDetailFragment::class.qualifiedName

        fun createFragment(teamId: Long): TeamDetailFragment {
            return TeamDetailFragment().apply {
                arguments = bundleOf(TEAM_ID_ARG to teamId)
            }
        }
    }

    private lateinit var module: TeamDetailModule
    private lateinit var component: TeamDetailComponent
    private var teamId by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        teamId = requireArguments().getLong(TEAM_ID_ARG)
        module = TeamDetailModule(teamId, TeamDetailRepositoryModule())

        component = TeamDetailComponent(
            module.store
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        component.onViewCreated(
            TeamDetailViewImpl(
                teamId = teamId,
                lifecycleScope = lifecycleScope,
                view = view,
                window = activity.window,
                isDarkMode = activity.isDarkMode()
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