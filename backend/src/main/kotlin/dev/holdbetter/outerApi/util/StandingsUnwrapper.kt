package dev.holdbetter.outerApi.util

import dev.holdbetter.outerApi.model.TeamRank
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

// TODO: Test
internal class StandingsUnwrapper :
    JsonTransformingSerializer<List<TeamRank>>(ListSerializer(TeamRank.serializer())) {

    companion object {
        const val L = "L"
        const val TABLES = "Tables"
        const val TEAM = "team"
    }

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return element.jsonObject[L]
            ?.jsonArray
            ?.firstOrNull()
            ?.jsonObject
            ?.get(TABLES)
            ?.jsonArray
            ?.firstOrNull()
            ?.jsonObject
            ?.get(TEAM) ?: element
    }
}