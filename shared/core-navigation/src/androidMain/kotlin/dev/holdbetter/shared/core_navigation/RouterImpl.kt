package dev.holdbetter.shared.core_navigation

import dev.holdbetter.shared.core_navigation.Destination.Standings.route

internal class RouterImpl(
    private val controller: NavigationController
) : Router {

    override fun navigateToStandings() {
        controller.navigate(Destination.Standings.route())
    }

    override fun navigateToTeam(teamId: Long) {
        controller.navigate(Destination.TeamDetail.route(teamId))
    }

    override fun back() {
        controller.navigateUp()
    }
}