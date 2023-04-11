package dev.holdbetter.shared.core_navigation

interface Router {
    fun handleThirdPartyLink(uri: String)
    fun navigateToStandings()
    fun navigateToTeam(teamId: Long, teamImageUrl: String)
    fun back()
}