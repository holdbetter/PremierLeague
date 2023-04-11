package dev.holdbetter.shared.core_navigation

import dev.holdbetter.shared.core_navigation.Destination.TeamDetail.Arguments.TEAM_ID
import dev.holdbetter.shared.core_navigation.Destination.TeamDetail.Arguments.TEAM_IMAGE

// TODO: Test
sealed interface Destination {

    val destination: String
    val argumentNames: Array<String>

    object Standings : Destination {

        override val argumentNames: Array<String> = emptyArray()
        override val destination: String = destination(Endpoint.Standings)
    }

    object TeamDetail : Destination {

        object Arguments {
            const val TEAM_ID = "team_id"
            const val TEAM_IMAGE = "team_image"
        }

        override val argumentNames: Array<String> = arrayOf(TEAM_ID, TEAM_IMAGE)
        override val destination: String = destination(Endpoint.TeamDetail)
    }

    fun Destination.destination(endpoint: Endpoint): String {
        return buildString {
            append(endpoint)
            for (argumentName in argumentNames) {
                append('/')
                append("{$argumentName}")
            }
        }
    }


    fun Destination.route(vararg arguments: Any): String {
        var replaced = destination

        argumentNames.forEachIndexed { index, argumentName ->
            replaced = replaced.replaceFirst(
                Regex.fromLiteral("{$argumentName}"),
                arguments[index].toString()
            )
        }

        return replaced
    }
}