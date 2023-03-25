package dev.holdbetter.shared.core_navigation

interface Router {
    fun navigateToStandings()
    fun navigateToTeam(teamId: Long)
    fun back()
}