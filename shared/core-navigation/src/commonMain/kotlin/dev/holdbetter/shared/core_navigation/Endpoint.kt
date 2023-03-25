package dev.holdbetter.shared.core_navigation

sealed interface Endpoint {

    val value: String

    object Standings : Endpoint {
        override val value: String = "standings"
        override fun toString() = buildEndpoint(Standings)
    }

    object TeamDetail : Endpoint {
        override val value: String = "team_detail"
        override fun toString() = buildEndpoint(TeamDetail)
    }

    // TODO: Test
    fun buildEndpoint(vararg endpoints: Endpoint) = if (endpoints.count() > 1) {
        buildString {
            endpoints.forEachIndexed { index, endpoint ->
                append(endpoint.value)
                if (index != endpoints.count() - 1) {
                    append('/')
                }
            }
        }
    } else {
        value
    }
}