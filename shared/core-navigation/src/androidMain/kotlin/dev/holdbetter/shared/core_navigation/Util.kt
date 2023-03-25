package dev.holdbetter.shared.core_navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.dynamicfeatures.createGraph
import dev.holdbetter.shared.core_navigation.Destination.Standings.route

fun NavController.createGraph(
    startDestination: Destination,
    startArguments: Array<Any> = emptyArray(),
    builder: NavGraphBuilder.() -> Unit
): NavGraph {
    val route = startDestination.route(*startArguments)

    return createGraph(
        route,
        null,
        builder
    )
}